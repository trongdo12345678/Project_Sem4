package com.admin.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.admin.repository.StatisticsRepository;
import com.customer.repository.ShoppingpageRepository;
import com.models.PageView;
import com.utils.Views;


@Controller
@RequestMapping("/admin/statistics")
public class StatisticsController {
    
    @Autowired
    StatisticsRepository repstat;
    
    @Autowired
    ShoppingpageRepository repspp;


    @GetMapping("/showpage")
    public String showpage(Model model) {
    	int[] idCategories = {};
		int[] idBrands = {};
		String[] statuses = { "NewRelease", "Active", "OutOfStock", "Inactive" };
        model.addAttribute("products", repspp.findAllnopaging(new PageView(), "", idCategories, idBrands, statuses));
        model.addAttribute("brands", repspp.findAllBrand());
        model.addAttribute("categories", repspp.findAllCate());
        repstat.getProfitAnalysis("monthly");
        repstat.getProfitAnalysis("quarterly");
        repstat.getProfitAnalysis("yearly");
        return Views.ADMIN_STATISTICSPAGE;
    }

    @GetMapping("/api/revenue/{period}")
    @ResponseBody
    public Map<String, Object> getRevenue(
            @PathVariable String period,
            @RequestParam(required = false) Long productId,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) Long brandId) {

        List<Map<String, Object>> data;
        
        if (productId != null) {
            data = repstat.getProductRevenue(period, productId);
        } else if (categoryId != null) {
            data = repstat.getCategoryRevenue(period, categoryId);
        } else if (brandId != null) {
            data = repstat.getBrandRevenue(period, brandId);
        } else {
        	data = repstat.getProductRevenue(period, productId);
        }

        data = repstat.getPredictedRevenue(data, period);
        
        List<String> labels = new ArrayList<>();
        List<Double> values = new ArrayList<>();
        List<Double> totalPeriodRevenue = new ArrayList<>();
        List<Double> percentage = new ArrayList<>();
        List<Boolean> isPredicted = new ArrayList<>();
        
        for (Map<String, Object> row : data) {
            labels.add(row.get("timePeriod").toString());
            values.add(((Number)row.get("SelectedRevenue")).doubleValue());
            totalPeriodRevenue.add(((Number)row.get("TotalRevenue")).doubleValue());
            percentage.add(((Number)row.get("SelectedPercentage")).doubleValue());
            isPredicted.add(Boolean.TRUE.equals(row.get("isPredicted")));
        }

        Map<String, Object> result = new HashMap<>();
        result.put("labels", labels);
        result.put("values", values);
        result.put("totalPeriodRevenue", totalPeriodRevenue);
        result.put("percentage", percentage);
        result.put("isPredicted", isPredicted);
        return result;
    }

  
    @GetMapping("/products/{period}")
    public ResponseEntity<Map<String, Object>> getProductStats(
            @PathVariable String period,
            @RequestParam(required = false) Integer productId) {
        try {
            Map<String, Object> response = new HashMap<>();
            
            // Lấy summary trend (dữ liệu cho chart)
            List<Map<String, Object>> summaryTrend = repstat.getProductSummaryTrend(period);
            
            // Lấy best selling products
            List<Map<String, Object>> bestSellers = repstat.getBestSellingProducts(period);
            
            // Lấy summary hiện tại (từ best sellers)
            Map<String, Object> latestSummary = new HashMap<>();
            if (!bestSellers.isEmpty()) {
                // Lấy dữ liệu từ sản phẩm đứng đầu của kỳ gần nhất
                Map<String, Object> topProduct = bestSellers.stream()
                    .filter(p -> ((Number)p.get("rank")).intValue() == 1)
                    .findFirst()
                    .orElse(new HashMap<>());
                    
                latestSummary.put("bestProduct", topProduct.get("productName"));
                latestSummary.put("bestProductRevenue", topProduct.get("revenue"));
                latestSummary.put("totalUnitsSold", topProduct.get("totalQuantity"));
                
                // Tính average price
                if (topProduct.get("totalQuantity") != null && topProduct.get("revenue") != null) {
                    double revenue = ((Number)topProduct.get("revenue")).doubleValue();
                    long quantity = ((Number)topProduct.get("totalQuantity")).longValue();
                    latestSummary.put("averagePrice", quantity > 0 ? revenue/quantity : 0);
                } else {
                    latestSummary.put("averagePrice", 0);
                }

                // Tính return rate
                if (topProduct.get("returnQuantity") != null && topProduct.get("totalQuantity") != null) {
                    long returnQty = ((Number)topProduct.get("returnQuantity")).longValue();
                    long totalQty = ((Number)topProduct.get("totalQuantity")).longValue();
                    double returnRate = totalQty > 0 ? (returnQty * 100.0 / totalQty) : 0;
                    latestSummary.put("returnRate", Math.round(returnRate * 100.0) / 100.0); // Round to 2 decimal places
                } else {
                    latestSummary.put("returnRate", 0.0);
                }
            }
            
            // Lấy top products
            List<Map<String, Object>> topProducts = repstat.getTopProducts(period);
            
            response.put("summary", latestSummary);
            response.put("summaryTrend", summaryTrend);
            response.put("topProducts", topProducts);
            response.put("bestSellers", bestSellers);
            response.put("period", period);
            response.put("totalPeriods", summaryTrend.size());
                     
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(
                        "error", "Failed to fetch product statistics",
                        "message", e.getMessage()
                    ));
        }
    }
    @GetMapping("/api/warehouse-revenue/{period}")
    @ResponseBody
    public Map<String, Object> getWarehouseRevenue(@PathVariable String period) {
        List<Map<String, Object>> rawData = repstat.getWarehouseRevenue(period);
        
        // Get unique warehouses and time periods
        Set<String> warehouses = new TreeSet<>();
        Set<String> timePeriods = new TreeSet<>();
        
        for (Map<String, Object> row : rawData) {
            warehouses.add(row.get("warehouseName").toString());
            timePeriods.add(row.get("timePeriod").toString());
        }

        // Calculate total revenue for each period
        Map<String, Double> periodTotals = new HashMap<>();
        for (Map<String, Object> row : rawData) {
            String periodd = row.get("timePeriod").toString();
            Double revenue = ((Number)row.get("NetRevenue")).doubleValue();
            periodTotals.merge(periodd, revenue, Double::sum);
        }

        Map<String, Object> result = new HashMap<>();
        result.put("data", rawData);  // Original data for table
        result.put("warehouses", new ArrayList<>(warehouses));  // List of warehouses
        result.put("labels", new ArrayList<>(timePeriods));  // Time periods
        result.put("periodTotals", periodTotals);  // Total revenue per period

        return result;
    }
    
    @GetMapping("/province-revenue/{period}")
    public ResponseEntity<Map<String, Object>> getProvinceRevenue(@PathVariable String period) {
        try {
            // Validate period
            if (!Arrays.asList("monthly", "quarterly", "yearly").contains(period)) {
                throw new IllegalArgumentException("Invalid period: " + period);
            }
            
            Map<String, Object> response = repstat.getProvinceRevenue(period);
            
            // Kiểm tra null và empty
            if (response == null || response.isEmpty()) {
                return ResponseEntity.noContent().build();
            }

            return ResponseEntity.ok(response);
            
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            // Log error
            return ResponseEntity.internalServerError().build();
        }
    }
    
    @GetMapping("/gross-profit/{period}")
    public ResponseEntity<?> getGrossProfitStats(@PathVariable String period) {
        List<Map<String, Object>> rawData = repstat.getGrossProfitAnalysis(period);
        
        Map<String, Object> response = new HashMap<>();
        
        // Group theo period để tránh duplicate và dùng periodTotalRevenue
        Map<String, Map<String, Object>> summaryByPeriod = new HashMap<>();
        
        for (Map<String, Object> row : rawData) {
            String timePeriod = row.get("timePeriod").toString();
            
            // Chỉ lấy một lần cho mỗi period
            if (!summaryByPeriod.containsKey(timePeriod)) {
                Map<String, Object> periodData = new HashMap<>();
                periodData.put("period", timePeriod);
                periodData.put("revenue", row.get("periodTotalRevenue")); // Dùng periodTotalRevenue
                periodData.put("cost", row.get("periodTotalCost")); // Dùng periodTotalCost
                
                // Tính profit và margin từ period totals
                double revenue = ((Number)row.get("periodTotalRevenue")).doubleValue();
                double cost = ((Number)row.get("periodTotalCost")).doubleValue();
                double profit = revenue - cost;
                double margin = revenue > 0 ? (profit * 100.0 / revenue) : 0;
                
                periodData.put("profit", profit);
                periodData.put("margin", margin);
                
                // Tính total units
                int units = ((Number)row.get("quantitySold")).intValue();
                periodData.put("units", units);
                
                summaryByPeriod.put(timePeriod, periodData);
            } else {
                // Cộng dồn units cho period
                Map<String, Object> periodData = summaryByPeriod.get(timePeriod);
                int currentUnits = ((Number)periodData.get("units")).intValue();
                int newUnits = ((Number)row.get("quantitySold")).intValue();
                periodData.put("units", currentUnits + newUnits);
            }
        }
        
        List<Map<String, Object>> monthlySummary = new ArrayList<>(summaryByPeriod.values());
        
        // Sort by period
        monthlySummary.sort((a, b) -> 
            ((String)a.get("period")).compareTo((String)b.get("period")));
        
        response.put("monthlySummary", monthlySummary);
        response.put("productPerformance", rawData);
        
        return ResponseEntity.ok(response);
    }
    @GetMapping("/profit-analysis")
    public ResponseEntity<List<Map<String, Object>>> getProfitAnalysis(@RequestParam String period) {
        List<Map<String, Object>> analysis = repstat.getProfitAnalysis(period);
        return ResponseEntity.ok(analysis);
    }
    @GetMapping("/profit-analysis-details")
    public ResponseEntity<Map<String, Object>> getProfitAnalysisDetails(
        @RequestParam String period,
        @RequestParam String selectedPeriod
    ) {
        try {
            List<Map<String, Object>> details = repstat.getProfitAnalysisDetails(period, selectedPeriod);
            
            // Tổ chức lại data cho frontend
            Map<String, Object> response = new HashMap<>();
            
            if (!details.isEmpty()) {
                response.put("revenue", details.get(0).get("revenue"));
                response.put("totalCost", details.get(0).get("total_cost"));
            }
            
            // Nhóm chi phí theo loại
            Map<String, List<Map<String, Object>>> costsByType = details.stream()
                .collect(Collectors.groupingBy(
                    d -> (String) d.get("cost_type"),
                    Collectors.mapping(
                        d -> Map.of(
                            "name", d.get("detail_name"),
                            "amount", d.get("amount"),
                            "typeTotal", d.get("type_total")
                        ),
                        Collectors.toList()
                    )
                ));
            
            response.put("costDetails", costsByType);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}