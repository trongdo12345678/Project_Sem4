package com.warehouseManager.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.businessManager.repository.RequestOrderRepository;
import com.models.Employee;
import com.models.Order;
import com.models.Order_detail;
import com.models.PageView;
import com.models.Product;
import com.models.Warehouse_releasenote;
import com.models.Warehouse_rn_detail;
import com.models.Request;
import com.models.Request_detail;
import com.utils.Views;
import com.warehouseManager.repository.ReleasenoteRepository;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("warehouseManager")
public class ReleasenoteController {

	@Autowired
	private RequestOrderRepository repoder;

	@Autowired
	private ReleasenoteRepository rele;
	
	@GetMapping("/orderByStatus")
	public String showStock(HttpSession session) {

	    Employee loggedInEmployee = (Employee) session.getAttribute("loggedInEmployee");

	    if (loggedInEmployee == null) {
	        return "redirect:/employee/login";
	    }

	    return Views.SHOW_ORDER_STATUS_WAITING;
	}

	
	@GetMapping("/OrderWaiting")
	@ResponseBody
	public List<Order> getOrdersByStatus(HttpSession session) {
	    Employee loggedInEmployee = (Employee) session.getAttribute("loggedInEmployee");

	    try {
	        if (loggedInEmployee != null) {
	            int employeeId = loggedInEmployee.getId();
	            return rele.getOrderByStatus(employeeId);
	        } else {
	            return new ArrayList<>();
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	        return new ArrayList<>();
	    }
	}
	
	@PostMapping("/completeDelivery/{orderId}")
	@ResponseBody
	public ResponseEntity<String> completeDelivery(@PathVariable int orderId) {
	    try {
	        boolean isUpdated = rele.completeDelivery(orderId);

	        if (isUpdated) {
	            return ResponseEntity.ok("Order status updated to 'Completed'");
	        } else {
	            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Order not found or update failed");
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while updating order status");
	    }
	}


	
	//show warehouse note in warehouseManager
	@GetMapping("/showWareReleasenote")
	public String showWareReleasenote(Model model, HttpSession session,
			@RequestParam(name = "cp", required = false, defaultValue = "1") int cp) {
	    PageView pv = new PageView();
	    pv.setPage_current(cp);
	    pv.setPage_size(8);
		Employee loggedInEmployee = (Employee) session.getAttribute("loggedInEmployee");

		if (loggedInEmployee != null) {
			int employeeId = loggedInEmployee.getId();

			List<Warehouse_releasenote> releasenotes = rele.findWareAllByEmployeeId(pv,employeeId);

			model.addAttribute("releasenotes", releasenotes);
			model.addAttribute("pv",pv);
		} else {
			model.addAttribute("error", "login");
		}

		return Views.SHOW_WAREHOUSE_RELEASENOTE;
	}
	
	//show warehouse note detail in warehouseManager

	  @GetMapping("/warehouseReleasenoteDetail") 
	  public String warehouseReleasenoteDetail(@RequestParam("id") int id, Model model) {
		  
	  Warehouse_releasenote releasenote = rele.findWarehouseReleasenoteById(id); 
	  List<Warehouse_rn_detail> details = rele.findWarehouseRnDetail(id);

	  model.addAttribute("releasenote", releasenote); 
	  model.addAttribute("details", details);	 	  
	  return Views.SHOW_WAREHOUSE_RELEASENOTE_DETAIL; 
	  }
	
	//show order in warehouseManager
	@GetMapping("/ShowInforOrder")
	public String ShowInforOrder(Model model, HttpSession session,
			@RequestParam(name = "cp", required = false, defaultValue = "1") int cp) {
	    PageView pv = new PageView();
	    pv.setPage_current(cp);
	    pv.setPage_size(8);
		Employee loggedInEmployee = (Employee) session.getAttribute("loggedInEmployee");
		
		Integer warehouseId = (Integer) session.getAttribute("warehouseId");
		
		if(loggedInEmployee != null) {					    
			List<Order> orders = rele.findAllOrderByEmployeeIdIsNull(pv, warehouseId);
			model.addAttribute("orders", orders);
			model.addAttribute("pv",pv);		
		}else {
			return "employee/login";
		}

		return Views.SHOW_ORDER_WAREHOUSE_RELEASENOTE;
	}
		
	  //show order_detail in warehouseManager	
	  @GetMapping("/showOrderDetail") 
	  public String showOrderDetail(@RequestParam("id") int id, Model model) {
		  
	  Order order = rele.findOrderById(id); 
	  List<Order_detail> details = rele.findOrderDetail(id);
	
	  model.addAttribute("order", order); 
	  model.addAttribute("details", details);	 	  
	  return Views.SHOW_ORDER_DETAIL; 
	  }
	

	  
	//show request in warehouseManager
	@GetMapping("/ShowOrderRequest")
	public String showRequest(Model model,
			@RequestParam(name = "cp", required = false, defaultValue = "1") int cp) {
	    PageView pv = new PageView();
	    pv.setPage_current(cp);
	    pv.setPage_size(10);
		List<Request> releasenotes = rele.findAllByEmployeeIdIsNull(pv);
		model.addAttribute("requests", releasenotes);
		model.addAttribute("pv",pv);
		return Views.SHOW_REQUEST_WAREHOUSE_RELEASENOTE;
	}

	  //show request_detail in warehouseManager
	  @GetMapping("/orderRequestdetails") 
	  public String showUpdateOrderDetailForm(@RequestParam("id") int id, Model model) {
		  
	  Request request = repoder.findRequestById(id); 
	  List<Request_detail> details = rele.findDetailsByRequestId(id);
	  model.addAttribute("request", request); 
	  model.addAttribute("details", details);	 	  
	  return Views.SHOW_REQUEST_WAREHOUSE_RELEASENOTE_DETAIL; 
	  }
	 
	 //receive request by employee
	@PostMapping("/receiveNote")
	public String receiveNote(@RequestParam("requestId") int requestId, HttpSession session) {

		Employee loggedInEmployee = (Employee) session.getAttribute("loggedInEmployee");
		
		Integer warehouseId = (Integer) session.getAttribute("warehouseId");

		if (loggedInEmployee != null) {
			
			int employeeId = loggedInEmployee.getId();
				        	        
	        rele.updateEmployeeId(requestId, employeeId, warehouseId);
			
			rele.updateStatusToProcessing(requestId);
		} else {
			return "redirect:/employee/login";
		}
		return "redirect:/warehouseManager/requestInWarehouseDetail?id=" + requestId;
	}
	
	//receive order by employee
	@PostMapping("/receiveInOrder")
	public String receiveInOrder(@RequestParam("id") int OrderId, HttpSession session) {

		Employee loggedInEmployee = (Employee) session.getAttribute("loggedInEmployee");

		if (loggedInEmployee != null) {
			int employeeId = loggedInEmployee.getId();
			rele.updateEmployeeIdInOrder(OrderId, employeeId);
			rele.updateStatusInOrder(OrderId);
			
		} else {
			return "employee/login";
		}
		return "redirect:/warehouseManager/orderInWarehouseDetail?id=" + OrderId;
	}
	
	// show order in warehouse note
	@GetMapping("/showOrderInWarehouse")
	public String showOrderInWarehouse(Model model, HttpSession session,
			@RequestParam(name = "cp", required = false, defaultValue = "1") int cp) {
	    PageView pv = new PageView();
	    pv.setPage_current(cp);
	    pv.setPage_size(8);
		Employee loggedInEmployee = (Employee) session.getAttribute("loggedInEmployee");

		if (loggedInEmployee != null) {
			int employeeId = loggedInEmployee.getId();
						
	        List<Order> orders = rele.findOrderByEmployeeId(pv, employeeId);

			model.addAttribute("orders", orders);
			model.addAttribute("pv",pv);
		} else {
			return "employee/login";
		}

		return Views.SHOW_ORDER_IN_WAREHOUSE_RELEASENOTE;
	}
	
	//show order and order_detail in warehouse note
	@GetMapping("/orderInWarehouseDetail")
	public String orderInWarehouseDetail(
			@RequestParam("id") int id,
			HttpSession session, Model model) {
		
		Employee loggedInEmployee = (Employee) session.getAttribute("loggedInEmployee");
		int employeeId = loggedInEmployee.getId();
			
		Order order = rele.findOrderByIdEmp(id, employeeId);
		    
		List<Order_detail> details = rele.findOrderDetail(id);
		    
		model.addAttribute("order", order);
		model.addAttribute("details", details);
		model.addAttribute("employeeId", employeeId);
		model.addAttribute("id", id);
	    return Views.SHOW_ORDER_IN_WAREHOUSE_DETAIL;
	}

	//show request in warehouse note
	@GetMapping("/showOrderRequestinWarehouse")
	public String showOrderRequestinWarehouse(Model model, HttpSession session,
			@RequestParam(name = "cp", required = false, defaultValue = "1") int cp) {
	    PageView pv = new PageView();
	    pv.setPage_current(cp);
	    pv.setPage_size(8);
	    
		Employee loggedInEmployee = (Employee) session.getAttribute("loggedInEmployee");
		
		Integer warehouseId = (Integer) session.getAttribute("warehouseId");

		if (loggedInEmployee != null) {
			int employeeId = loggedInEmployee.getId();
			
	        List<Request> requests = rele.findAllByEmployeeId(pv, employeeId, warehouseId);

			model.addAttribute("requests", requests);
			model.addAttribute("pv",pv);
		} else {
			model.addAttribute("error", "login");
		}

		return Views.SHOW_ORDER_REQUEST_IN_WAREHOUSE;
	}
	
	//show request and order where warehouse note not 
	@GetMapping("/showOrderAndRequest")
	public String showOrderAndRequest(Model model, HttpSession session,
			@RequestParam(name = "cp", required = false, defaultValue = "1") int cp) {
	    PageView pv = new PageView();
	    pv.setPage_current(cp);
	    pv.setPage_size(8);
		Employee loggedInEmployee = (Employee) session.getAttribute("loggedInEmployee");

		if (loggedInEmployee != null) {
			int employeeId = loggedInEmployee.getId();

	        List<Request> requests = rele.findAllByEmployeeIdAndType(pv, employeeId);

			model.addAttribute("requests", requests);
			model.addAttribute("pv",pv);
		} else {
			model.addAttribute("error", "login");
		}

		return Views.SHOW_ORDER_AND_REQUEST;
	}
	
	@GetMapping("/insufficientOutputDetail")
	public String insufficientOutputDetail(
			@RequestParam("id") int id,
			HttpSession session, Model model) {
		
		Employee loggedInEmployee = (Employee) session.getAttribute("loggedInEmployee");
		int employeeId = loggedInEmployee.getId();
			
		Request request = rele.findRequestByIdEmp(id, employeeId);
		    
		List<Request_detail> details = rele.findDetailsByRequestId(id);
		    
		model.addAttribute("request", request);
		model.addAttribute("details", details);
		model.addAttribute("employeeId", employeeId);
		model.addAttribute("requestId", id);
	    return Views.SHOW_INSUFFICIENT_OUTPUT_DETAIL;
	}
	
	//show request and request_detail in warehouse 
	@GetMapping("/requestInWarehouseDetail")
	public String requestInWarehouseDetail(
			@RequestParam("id") int id,
			HttpSession session, Model model) {
		
		Employee loggedInEmployee = (Employee) session.getAttribute("loggedInEmployee");
		int employeeId = loggedInEmployee.getId();
			
		Request request = rele.findRequestByIdEmp(id, employeeId);
		    
		List<Request_detail> details = rele.findDetailsByRequestId(id);
		    
		model.addAttribute("request", request);
		model.addAttribute("details", details);
		model.addAttribute("employeeId", employeeId);
		model.addAttribute("requestId", id);
	    return Views.SHOW_REQUEST_IN_WAREHOUSE_DETAIL;

	}

	//show add all release note by request
	@GetMapping("/showAddAllWarehouseRelesenote")
	public String showAddAllWarehouseRelesenote(
	        @RequestParam("employeeId") int employeeId,
	        @RequestParam("requestId") int requestId,
	        @RequestParam("name") String name, 
	        
	        Model model) {

	    Request request = rele.findRequestById(requestId); 
	    List<Request_detail> details = rele.findDetailsByRequestId(requestId);
	    
        String randomName = "WRR-" + UUID.randomUUID().toString().substring(0, 8);
        request.setName(randomName);

	    model.addAttribute("request", request);
	    model.addAttribute("details", details);
	    model.addAttribute("employeeId", employeeId);
	    model.addAttribute("requestId", requestId);
	    model.addAttribute("name", name);
	    

	    return Views.ADD_ALL_WAREHOUSE_RELEASENOTE;
	}

	
    //show add release note by request
	@GetMapping("/showAddWarehouseRelesenote")
	public String showAddWarehouseReleasenote(
			@RequestParam("employeeId") int employeeId,
			@RequestParam("requestId") int requestId, 
			Model model) {
		
		Warehouse_releasenote releasenote = new Warehouse_releasenote();
		Request request = repoder.findRequestById(requestId); 
	  	List<Request_detail> requestDetails  = rele.findDetailsByRequestId(requestId);
	  	
        String randomName = "WRR-" + UUID.randomUUID().toString().substring(0, 8);
        request.setName(randomName);
        
        List<Request_detail> filteredDetails = requestDetails.stream()
                .filter(detail -> detail.getQuantity_exported() < detail.getQuantity_requested())
                .collect(Collectors.toList());

	  	model.addAttribute("request", request); 
	  	model.addAttribute("details", requestDetails);	
	  	model.addAttribute("filteredDetails", filteredDetails);	
		model.addAttribute("employeeId", employeeId);
		model.addAttribute("requestId", requestId);
		model.addAttribute("releasenotes", releasenote);
		return Views.ADD_WAREHOUSE_RELEASENOTE;
	}

	//add release note by request
	@PostMapping("/addWarehouseRelesenote")
	public String addWarehouseReleasenote(
			@RequestParam("name") String name, 
			@RequestParam("statusWr") String statusWr,
			@RequestParam("requestId") int requestId, 
			@RequestParam("employeeId") int employeeId,		
			@RequestParam(required = false) List<Integer> id_product,
			@RequestParam(required = false) List<Integer> quantity, 
			@RequestParam(required = false) List<String> status,
			@RequestParam(required = false) List<Integer> Id,
			Model model, HttpSession session,RedirectAttributes redirectAttributes) {
		
		Integer warehouseId = (Integer) session.getAttribute("warehouseId");

		Warehouse_releasenote releasenote = new Warehouse_releasenote();
		releasenote.setName(name);
		releasenote.setDate(LocalDateTime.now());
		releasenote.setStatusWr(statusWr);
		releasenote.setRequest_id(requestId);
		releasenote.setEmployee_Id(employeeId);

		List<Warehouse_rn_detail> details = new ArrayList<>();
		if (id_product != null && !id_product.isEmpty()) {
			for (int i = 0; i < id_product.size(); i++) {					
				if (quantity.get(i) > 0) { 
		            Warehouse_rn_detail detail = new Warehouse_rn_detail();
		            detail.setId_product(id_product.get(i));
		            detail.setQuantity(quantity.get(i));
		            detail.setStatus(status.get(i));		            
		            details.add(detail);
	            
		        }
			}
		}
		
		
		List<Request_detail> rsdetail = new ArrayList<>();

		if (id_product != null && !id_product.isEmpty()) { 


		    for (int i = 0; i < id_product.size(); i++) { 
		        Request_detail detail = new Request_detail();

		        int productId = id_product.get(i); 
		        int quantityExported = quantity.get(i); 
		        System.out.println("Idzzzz: " + Id);
		        System.out.println("id_product: " + id_product);
		        System.out.println("quantity: " + quantity);

		        if (Id.size() > i) {
		            int detailId = Id.get(i);
		            detail.setId(detailId);                
		            detail.setQuantity_exported(quantityExported); 
		            detail.setId_product(productId); 
		            
		            rsdetail.add(detail);

		            rele.updateQuantityExportedz(detailId, productId, requestId, quantityExported);
		        } 
		    }
		}
	 

		boolean isSaved = rele.addWarehouseReleasenote(releasenote, details, warehouseId);
		
		if (!isSaved) {
			redirectAttributes.addFlashAttribute("error",  "Inventory quantity is not enough for export");
	        return "redirect:showAddWarehouseRelesenote?employeeId=" + employeeId + "&requestId=" + requestId;
	    }
		
	    if (isSaved) {
			

	        if (id_product != null && !id_product.isEmpty()) {

		        for (int i = 0; i < id_product.size(); i++) {		        			        	
		        	
		            int idProduct = id_product.get(i);
		            if (Id.size() > i) {
			            int detailId = Id.get(i);			                           			            
			            rele.updateStatusRequestDetail(detailId, requestId, idProduct); 
		            }
		        }	        	
	        }    	
	        int orderId = rele.findOrderIdByRequest(requestId);
	        rele.isRequestComplete(requestId, orderId);        
	    }	
	    return "redirect:insufficientOutputDetail?id=" + requestId;
	}

	
	 //delete request
    @GetMapping("/deleteRequest")
    public String deleteRequest(@RequestParam("id") int requestId) {
    	rele.deleteEmployeeIdByRequestId(requestId);
        return "redirect:showOrderRequestinWarehouse";
    }
	
	// show add all release note by order
	@GetMapping("/showAddAllOrderRelesenote")
	public String showAddAllOrderRelesenote(
	        @RequestParam("employeeId") int employeeId,
	        @RequestParam("id") int id,	        
	        @RequestParam("orderID") String orderID,
	        Model model) {

	    Order order = rele.findOrderById(id); 
	    List<Order_detail> details = rele.findOrderDetail(id);

	    model.addAttribute("order", order);
	    model.addAttribute("details", details);
	    model.addAttribute("employeeId", employeeId);
	    model.addAttribute("id", id);
	    model.addAttribute("orderID", orderID);

	    return Views.ADD_ALL_ORDER_RELEASENOTE;
	}
	
    //show add release note by Order
	@GetMapping("/showAddOrderRelesenote")
	public String showAddOrderRelesenote(
			@RequestParam("employeeId") int employeeId,
			@RequestParam("id") int id, 
			Model model) {
		
		Warehouse_releasenote releasenote = new Warehouse_releasenote();
		Order order = rele.findOrderById(id); 
	  	List<Order_detail> details = rele.findOrderDetail(id);
	  		
	  	model.addAttribute("order", order); 
	  	model.addAttribute("details", details);	
		model.addAttribute("employeeId", employeeId);
		model.addAttribute("id", id);
		model.addAttribute("releasenotes", releasenote);
		return Views.ADD_WAREHOUSE_RELEASENOTE_BY_ORDER;
	}

	//add release note by order
	@PostMapping("/addRelesenoteByOrder")
	public String addRelesenoteByOrder(
			@RequestParam("orderID") String orderID, 
			@RequestParam("statusWr") String statusWr,
			@RequestParam("id") int id, 
			@RequestParam("employeeId") int employeeId,
			@RequestParam(required = false) List<Integer> id_product,
			@RequestParam(required = false) List<Integer> quantity_Ex,
			@RequestParam(required = false) List<Integer> quantity_Rq,
			@RequestParam(required = false) List<String> status,
			Model model, HttpSession session,RedirectAttributes redirectAttributes) {
				

		Warehouse_releasenote releasenote = new Warehouse_releasenote();
		releasenote.setName(orderID);
		releasenote.setDate(LocalDateTime.now());
		releasenote.setStatusWr(statusWr);
		releasenote.setOrder_id(id);
		releasenote.setEmployee_Id(employeeId);
		
		Integer warehouseId = (Integer) session.getAttribute("warehouseId");
				
		
		List<Warehouse_rn_detail> details = new ArrayList<>();
		if (id_product != null && !id_product.isEmpty()) {
			for (int i = 0; i < id_product.size(); i++) {
				if (quantity_Ex.get(i) > 0) {
				Warehouse_rn_detail detail = new Warehouse_rn_detail();
				detail.setId_product(id_product.get(i));
				detail.setQuantity(quantity_Ex.get(i));				
				detail.setStatus(status.get(i));
				details.add(detail);
				}
			}
		}

		boolean isSaved = rele.addWarehouseReleasenoteByOrder(releasenote, details,warehouseId);
		
		if (!isSaved) {
			redirectAttributes.addFlashAttribute("error",  "Inventory quantity is not enough for export");
	        return "redirect:showAddOrderRelesenote?employeeId=" + employeeId + "&id=" + id;
	    }
					
	    if (isSaved) {
	        int releaseNoteId = releasenote.getId(); 	        
	        if( rele.isOrderComplete(releaseNoteId, id) == false) {
	        	Request rq = new Request();
	            String randomName = "WRO-" + UUID.randomUUID().toString().substring(0, 8);
	            
	        	rq.setName(randomName);
	        	rq.setOrder_id(id);
	        	rq.setDate(LocalDateTime.now());
	        	rq.setType("Order");
	        	rq.setEmployee_Id(employeeId);
	        	rq.setWarehouse_Id(0);
	        	rq.setStatusRequest("Processing");

	        	List<Request_detail> rqdts = new ArrayList<>();
	    		if (id_product != null && !id_product.isEmpty()) {
	    			for (int i = 0; i < id_product.size(); i++) {
	    				
	    				Request_detail detail = new Request_detail();
	                    detail.setId_product(id_product.get(i));
	                    detail.setQuantity_exported(quantity_Ex.get(i));
	                    detail.setQuantity_requested(quantity_Rq.get(i));
	                    String currentStatus; 
	                    if (quantity_Ex.get(i) >= quantity_Rq.get(i)) {
	                        currentStatus = "Completed";
	                    } else {
	                        currentStatus = "Processing";
	                    }
	                    detail.setStatus(currentStatus);
	                    rqdts.add(detail);
	    			}
	    		}
	    		
	    	boolean saveRs = repoder.addRequestOrderWithDetails(rq, rqdts);
	    	
			
			if (saveRs) {
				
				int requestId = rq.getId();				
			
				return "redirect:insufficientOutputDetail?id=" + requestId; 
				}
			 
	        }	
	        rele.isRleComplete(releaseNoteId,id);
	    }		
		return "redirect:orderInWarehouseDetail?id=" + id;
	}
	
    // delete order
    @GetMapping("/deleteOrder")
    public String deleteOrder(@RequestParam("id") int orderId) {
    	rele.deleteEmployeeIdByOrderId(orderId);
        return "redirect:showOrderInWarehouse";
    }
	
	//find name table product
	@GetMapping("/getProducts")
	@ResponseBody
	public List<Product> getProducts() {
		return repoder.findAllProduct();
	}

}
