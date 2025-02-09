
	let currentChart = null;
	let topProductsChart = null;
	let currentPeriod = 'monthly';
	function showLoading(selector) {
	    $(selector).css('opacity', '0.5');
	    // Có thể thêm loading spinner nếu cần
	}

	function hideLoading(selector) {
	    $(selector).css('opacity', '1');
	}

	function showToast(type, message) {
	    // Implement toast notification
	    console.log(`${type}: ${message}`);
	}
	
	

	function changePeriod(period) {
	    currentPeriod = period;
	    
	    // Remove active class from all buttons
	    document.querySelectorAll('.period-btn').forEach(btn => {
	        btn.classList.remove('active');
	    });
	    
	    // Add active class to clicked button
	    document.querySelector(`.period-btn[onclick*="${period}"]`).classList.add('active');
	    
	    // Load data based on active tab
	    const activeTab = document.querySelector('.tab-pane.active');
	    if (activeTab.id === 'revenueTab') {
	        loadChart(period);
	    } else if (activeTab.id === 'productsTab') {
	        loadProductStats(period); // Thêm period parameter
	    } else if (activeTab.id === 'warehouseTab') {
	        loadWarehouseData(period); // Thêm period parameter nếu cần
	    }
	}
	function getRandomColor() {
	    const letters = '0123456789ABCDEF';
	    let color = '#';
	    for (let i = 0; i < 6; i++) {
	        color += letters[Math.floor(Math.random() * 16)];
	    }
	    return color;
	}
	function loadChart(period) {
	    currentPeriod = period;
	    const productId = document.getElementById('productSelect').value;
	    const url = `/admin/statistics/api/revenue/${period}${productId ? `?productId=${productId}` : ''}`;
	    
	    document.querySelectorAll('.period-btn').forEach(btn => {
	        btn.classList.remove('active');
	    });
	    document.querySelector(`.period-btn[onclick*="${period}"]`).classList.add('active');
	    
	    fetch(url)
	        .then(response => response.json())
	        .then(data => {
	            if (currentChart) {
	                currentChart.destroy();
	            }

	            const chartElement = document.getElementById('revenueChart');
	            Chart.register(ChartDataLabels);
	            
	            const datasets = [];
	            
	            if (productId) {
	                // Product Revenue (bottom)
	                datasets.push({
	                    label: 'Selected Product Revenue',
	                    data: data.values,
	                    backgroundColor: 'rgba(255, 99, 132, 0.7)',
	                    borderColor: 'rgba(255, 99, 132, 1)',
	                    borderWidth: 1,
	                    datalabels: {
	                        anchor: 'end',
	                        align: 'bottom',
	                        formatter: function(value, context) {
	                            if (value === 0) return '';
	                            return `${data.percentage[context.dataIndex].toFixed(1)}%\n$${value.toLocaleString()}`;
	                        },
	                        font: { 
	                            weight: 'bold', 
	                            size: 14 
	                        },
	                        color: '#000000',
	                        padding: 8,
	                        backgroundColor: 'rgba(255, 255, 255, 0.7)',
	                        borderRadius: 4
	                    }
	                });

	                // Other Revenue (top)
	                const otherRevenue = data.totalPeriodRevenue.map((total, index) => total - data.values[index]);
	                datasets.push({
	                    label: 'Other Revenue',
	                    data: otherRevenue,
	                    backgroundColor: 'rgba(54, 162, 235, 0.5)',
	                    borderColor: 'rgba(54, 162, 235, 1)',
	                    borderWidth: 1,
	                    datalabels: {
	                        anchor: 'start',
	                        align: 'top',
	                        formatter: function(value, context) {
	                            if (value === 0) return '';
	                            const percentage = (100 - data.percentage[context.dataIndex]).toFixed(1);
	                            return `${percentage}%\n$${value.toLocaleString()}`;
	                        },
	                        font: { 
	                            weight: 'bold', 
	                            size: 14 
	                        },
	                        color: '#000000',
	                        padding: 8,
	                        backgroundColor: 'rgba(255, 255, 255, 0.7)',
	                        borderRadius: 4
	                    }
	                });
	            } else {
	                datasets.push({
	                    label: 'Total Revenue',
	                    data: data.values,
	                    backgroundColor: 'rgba(54, 162, 235, 0.5)',
	                    borderColor: 'rgba(54, 162, 235, 1)',
	                    borderWidth: 1,
	                    datalabels: {
	                        anchor: 'end',
	                        align: 'top',
	                        formatter: function(value) {
	                            if (value === 0) return '';
	                            return `$${value.toLocaleString()}`;
	                        },
	                        font: { 
	                            weight: 'bold', 
	                            size: 14 
	                        },
	                        color: '#000000',
	                        padding: 8,
	                        backgroundColor: 'rgba(255, 255, 255, 0.7)',
	                        borderRadius: 4
	                    }
	                });
	            }

	            currentChart = new Chart(chartElement, {
	                type: 'bar',
	                data: {
	                    labels: data.labels,
	                    datasets: datasets
	                },
	                options: {
	                    responsive: true,
	                    maintainAspectRatio: false,
	                    plugins: {
	                        title: {
	                            display: true,
	                            text: `Revenue Statistics (${period.charAt(0).toUpperCase() + period.slice(1)})`,
	                            font: { size: 16 }
	                        },
	                        legend: { 
	                            position: 'top',
	                        },
	                        tooltip: {
	                            callbacks: {
	                                label: function(context) {
	                                    const datasetLabel = context.dataset.label;
	                                    const value = context.raw;
	                                    
	                                    if (datasetLabel === 'Selected Product Revenue') {
	                                        const percentage = data.percentage[context.dataIndex];
	                                        return `${datasetLabel}: $${value.toLocaleString()} (${percentage.toFixed(2)}%)`;
	                                    }
	                                    if (datasetLabel === 'Other Revenue') {
	                                        const percentage = 100 - data.percentage[context.dataIndex];
	                                        return `${datasetLabel}: $${value.toLocaleString()} (${percentage.toFixed(2)}%)`;
	                                    }
	                                    return `${datasetLabel}: $${value.toLocaleString()}`;
	                                }
	                            }
	                        }
	                    },
	                    scales: {
	                        x: {
	                            stacked: true
	                        },
	                        y: {
	                            stacked: true,
	                            beginAtZero: true,
	                            ticks: {
	                                callback: value => '$' + value.toLocaleString()
	                            }
	                        }
	                    }
	                }
	            });
	        });
	}

	document.addEventListener('DOMContentLoaded', function() {
	    document.querySelector('.period-btn[onclick*="monthly"]').classList.add('active');
	    loadChart('monthly');
	});
	
	let productChart = null;

	function loadProductStats(period = currentPeriod) { // Thêm default parameter
	    $("#loading").show();
	    $.ajax({
	        url: `/admin/statistics/products/${period}`,
	        method: "GET",
	        dataType: "json"
	    })
	    .done(function(response) {
	        if (response) {
	            updateProductSummary(response.summary);
	            updateProductChart(response.summaryTrend);
	            updateTopProductsTable(response.topProducts);
	            updateTopProductsChart(response.topProducts);
	        }
	    })
	    .fail(function(error) {
	        console.error("Error loading stats:", error);
	        toastr.error("Failed to load statistics");
	    })
	    .always(function() {
	        $("#loading").hide();
	    });
	}

	function updateProductSummary(summary) {
	    if (!summary) return;
	    
	    $('#bestProduct').text(summary.bestProduct || '-');
	    $('#bestProductRevenue').text('$' + formatNumber(summary.bestProductRevenue || 0));
	    $('#totalUnitsSold').text(formatNumber(summary.totalUnitsSold || 0));
	    $('#averagePrice').text('$' + formatNumber(summary.averagePrice || 0, 2));
	    $('#returnRate').text(formatNumber(summary.returnRate || 0, 1) + '%');
	}
	function formatProductPeriodLabel(period) {
	    if (!period) return '';
	    
	    // Reverse the period before formatting
	    if (period.includes('-Q')) {
	        const [year, quarter] = period.split('-Q');
	        return `Q${quarter} ${year}`; // Shorter format for chart
	    } else if (period.includes('-')) {
	        const [year, month] = period.split('-');
	        return `${month}/${year}`; // Shorter format for chart
	    } else {
	        return period;
	    }
	}
	function updateProductChart(summaryTrend) {
	    if (!summaryTrend || !summaryTrend.length) return;

	    const ctx = document.getElementById('productChart');
	    if (productChart) {
	        productChart.destroy();
	    }

	    // Sắp xếp dữ liệu theo thời gian tăng dần
	    const sortedData = [...summaryTrend].sort((a, b) => {
	        return new Date(a.timePeriod) - new Date(b.timePeriod);
	    });

	    const periods = sortedData.map(item => formatProductPeriodLabel(item.timePeriod));
	    const unitsSold = sortedData.map(item => item.totalUnitsSold);
	    const avgPrices = sortedData.map(item => item.averagePrice);
	    const returnRates = sortedData.map(item => item.returnRate);

	    productChart = new Chart(ctx, {
	        type: 'bar',
	        data: {
	            labels: periods,
	            datasets: [
	                {
	                    label: 'Units Sold',
	                    data: unitsSold,
	                    backgroundColor: 'rgba(54, 162, 235, 0.5)',
	                    borderColor: 'rgba(54, 162, 235, 1)',
	                    borderWidth: 1,
	                    yAxisID: 'y'
	                },
	                {
	                    label: 'Average Price ($)',
	                    data: avgPrices,
	                    type: 'line',
	                    borderColor: 'rgba(255, 99, 132, 1)',
	                    tension: 0.1,
	                    yAxisID: 'y1'
	                },
	                {
	                    label: 'Return Rate (%)',
	                    data: returnRates,
	                    type: 'line',
	                    borderColor: 'rgba(75, 192, 192, 1)',
	                    tension: 0.1,
	                    yAxisID: 'y2'
	                }
	            ]
	        },
	        options: {
	            responsive: true,
	            maintainAspectRatio: false,
	            interaction: {
	                mode: 'index',
	                intersect: false,
	            },
	            scales: {
	                x: {
	                    reverse: false
	                },
	                y: {
	                    type: 'linear',
	                    position: 'left',
	                    title: {
	                        display: true,
	                        text: 'Units Sold'
	                    }
	                },
	                y1: {
	                    type: 'linear',
	                    position: 'right',
	                    title: {
	                        display: true,
	                        text: 'Average Price ($)'
	                    },
	                    grid: {
	                        drawOnChartArea: false
	                    }
	                },
	                y2: {
	                    type: 'linear',
	                    position: 'right',
	                    title: {
	                        display: true,
	                        text: 'Return Rate (%)'
	                    },
	                    grid: {
	                        drawOnChartArea: false
	                    }
	                }
	            }
	        }
	    });
	}

	

	function updateTopProductsTable(topProducts) {
	    if (!topProducts || !topProducts.length) return;

	    const tableBody = $('#topProductsTable');
	    let currentPeriod = null;
	    let isEvenGroup = true;
	    let periodTotal = {};
	    
	    // Calculate totals for each period
	    topProducts.forEach(product => {
	        if (!periodTotal[product.timePeriod]) {
	            periodTotal[product.timePeriod] = {
	                revenue: 0,
	                unitsSold: 0
	            };
	        }
	        periodTotal[product.timePeriod].revenue += parseFloat(product.revenue || 0);
	        periodTotal[product.timePeriod].unitsSold += parseInt(product.unitsSold || 0);
	    });
	    
	    const rows = topProducts.map(product => {
	        let rowHtml = '';
	        
	        // Add period header row
	        if (currentPeriod !== product.timePeriod) {
	            currentPeriod = product.timePeriod;
	            isEvenGroup = !isEvenGroup;
	            const total = periodTotal[currentPeriod];
	            
	            rowHtml += `
	                <tr class="period-header ${isEvenGroup ? 'even-period' : 'odd-period'}">
	                    <td colspan="2"><strong>${formatPeriodLabel(product.timePeriod)}</strong></td>
	                    <td><strong>$${formatNumber(total.revenue)}</strong></td>
	                    <td><strong>${total.unitsSold}</strong></td>
	                </tr>
	            `;
	        }
	        
	        // Add product row
	        rowHtml += `
	            <tr class="${isEvenGroup ? 'even-period' : 'odd-period'}">
	                <td></td>
	                <td>${product.productName || 'N/A'}</td>
	                <td>$${formatNumber(product.revenue || 0)}</td>
	                <td>${parseInt(product.unitsSold || 0)}</td>
	            </tr>
	        `;
	        
	        return rowHtml;
	    }).join('');
	    
	    tableBody.html(rows);
	}

	
	function getMonthName(month) {
	    const months = [
	        'January', 'February', 'March', 'April', 'May', 'June',
	        'July', 'August', 'September', 'October', 'November', 'December'
	    ];
	    return months[month - 1];
	}
	function formatCurrency(value) {
	    return new Intl.NumberFormat('vi-VN', { 
	        style: 'currency', 
	        currency: 'VND' 
	    }).format(value);
	}

	function formatNumber(value) {
	    return new Intl.NumberFormat('en-US', {
	        minimumFractionDigits: 2,
	        maximumFractionDigits: 2
	    }).format(value);
	}


	function formatPercent(value) {
	    return new Intl.NumberFormat('vi-VN', { 
	        style: 'percent',
	        minimumFractionDigits: 1,
	        maximumFractionDigits: 1 
	    }).format(value / 100);
	}

	

	
	$(document).ready(function() {
	    loadProductStats('monthly');
	});
	$('a[data-bs-toggle="tab"]').on('shown.bs.tab', function (e) {
	    const targetTab = e.target.getAttribute('href');
	    if (targetTab === '#productsTab' && !productChart) {
	        loadProductStats(currentPeriod);
	    } else if (targetTab === '#revenueTab') {
	        loadChart(currentPeriod);
	    } else if (targetTab === '#warehouseTab') {
	        loadWarehouseData(currentPeriod);
	    }
	});
	$(document).ready(function() {
	    const activeTab = document.querySelector('.tab-pane.active');
	    if (activeTab) {
	        if (activeTab.id === 'productsTab') {
	            loadProductStats(currentPeriod);
	        } else if (activeTab.id === 'revenueTab') {
	            loadChart(currentPeriod);
	        } else if (activeTab.id === 'warehouseTab') {
	            loadWarehouseData(currentPeriod);
	        }
	    }
	});
	
	let warehouseChart = null;

	function loadWarehouseData() {
	   
	    
	    fetch(`/admin/statistics/warehouse-revenue/${currentPeriod}`)
	        .then(response => {
	            if (!response.ok) {
	                console.log('Response status:', response.status); // Debug log
	                console.log('Response headers:', response.headers); // Debug log
	                throw new Error(`HTTP error! status: ${response.status}`);
	            }
	            return response.json();
	        })
	        .then(data => {
	            
	            if (Array.isArray(data)) {
	                updateWarehouseChart(data);
	                updateWarehouseTable(data);
	            } else {
	               
	            }
	        })
	        .catch(error => {
	          
	            const tableBody = document.getElementById('warehouseTableBody');
	            if (tableBody) {
	                tableBody.innerHTML = '<tr><td colspan="5">Error loading data. Please try again later.</td></tr>';
	            }
	        });
	}

	function updateWarehouseChart(data) {
	    if (!data || data.length === 0) return;

	    const ctx = document.getElementById('warehouseChart');
	    if (warehouseChart) {
	        warehouseChart.destroy();
	    }

	    // Tổ chức lại data
	    const timePeriods = [...new Set(data.map(item => item.timePeriod))].sort();
	    const warehouses = [...new Set(data.map(item => item.warehouseName))];
	    
	    // Tính tổng doanh thu cho mỗi period
	    const periodTotals = timePeriods.reduce((acc, period) => {
	        const periodData = data.filter(item => item.timePeriod === period);
	        acc[period] = periodData.reduce((sum, item) => sum + item.revenue, 0);
	        return acc;
	    }, {});

	    
	    const datasets = warehouses.map((warehouse, index) => {
	        return {
	            label: warehouse,
	            data: timePeriods.map(period => {
	                const record = data.find(item => 
	                    item.timePeriod === period && 
	                    item.warehouseName === warehouse
	                );
	                return record ? record.revenue : 0;
	            }),
	            backgroundColor: getColor(index, 0.7),
	            borderColor: getColor(index, 1),
	            borderWidth: 1
	        };
	    });

	    warehouseChart = new Chart(ctx, {
	        type: 'bar',
	        data: {
	            labels: timePeriods,  // Đơn giản hóa labels
	            datasets: datasets
	        },
	        options: {
	            responsive: true,
	            maintainAspectRatio: false,
	            layout: {
	                padding: {
	                    top: 40,
	                    right: 160,
	                    bottom: 40,
	                    left: 20
	                }
	            },
	            scales: {
	                x: {
	                    stacked: true,
	                    grid: {
	                        display: false
	                    }
	                },
	                y: {
	                    stacked: true,
	                    beginAtZero: true,
	                    ticks: {
	                        callback: value => '$' + value.toLocaleString()
	                    }
	                }
	            },
	            plugins: {
	                legend: {
	                    position: 'right',
	                    align: 'start'
	                },
	                tooltip: {
	                    callbacks: {
	                        label: function(context) {
	                            return `${context.dataset.label}: $${context.raw.toLocaleString()} (${(context.raw/periodTotals[context.label]*100).toFixed(1)}%)`;
	                        }
	                    }
	                }
	            }
	        },
	        plugins: [{
	            id: 'totalLabel',
	            afterDraw: function(chart) {
	                const ctx = chart.ctx;
	                ctx.save();
	                ctx.font = 'bold 14px Arial';
	                ctx.textAlign = 'center';
	                ctx.fillStyle = '#333';

	                chart.data.labels.forEach((label, i) => {
	                    const meta = chart.getDatasetMeta(chart.data.datasets.length - 1);
	                    if (!meta.data[i] || !meta.data[i].x) return;
	                    
	                    const x = meta.data[i].x;
	                    const yTop = Math.min(...chart.data.datasets.map(dataset => {
	                        const datasetMeta = chart.getDatasetMeta(datasets.indexOf(dataset));
	                        return datasetMeta.data[i]?.y ?? chart.chartArea.bottom;
	                    }));
	                    
	                    const total = periodTotals[label];
	                    ctx.fillText('$' + total.toLocaleString(), x, yTop - 15);
	                });
	                
	                ctx.restore();
	            }
	        }]
	    });

	    
	    ctx.parentElement.style.height = '500px';
	    ctx.parentElement.style.width = '100%';
	}

	
	function formatPeriodLabel(period) {
    if (period.includes('-Q')) {
        
        const [year, quarter] = period.split('-Q');
        return `Quarter ${quarter} ${year}`;
    } else if (period.includes('-')) {
       
        const [year, month] = period.split('-');
        const date = new Date(year, parseInt(month) - 1);
        return date.toLocaleDateString('en-US', { 
            month: 'long',
            year: 'numeric'
        });
    } else {
        
        return `Year ${period}`;
    }
}
    
    


	function getColor(index, alpha) {
	    const colors = [
	        `rgba(255, 99, 132, ${alpha})`,  
	        `rgba(54, 162, 235, ${alpha})`,   
	        `rgba(255, 206, 86, ${alpha})`,   
	        `rgba(75, 192, 192, ${alpha})`,  
	        `rgba(153, 102, 255, ${alpha})`,  
	        `rgba(255, 159, 64, ${alpha})`,   
	        `rgba(201, 203, 207, ${alpha})`   
	    ];
	    return colors[index % colors.length];
	}

	function updateWarehouseTable(data) {
	    const tableBody = document.getElementById('warehouseTableBody');
	    if (!tableBody || !data.length) return;

	   
	    const sortedData = [...data].sort((a, b) => {
	        if (a.timePeriod !== b.timePeriod) {
	            return a.timePeriod - b.timePeriod;
	        }
	        return b.percentage - a.percentage;
	    });

	    let currentPeriod = null;
	    let isEvenGroup = true;

	    tableBody.innerHTML = sortedData.map(item => {
	        if (currentPeriod !== item.timePeriod) {
	            currentPeriod = item.timePeriod;
	            isEvenGroup = !isEvenGroup;
	        }
	        
	        return `
	            <tr class="${isEvenGroup ? 'even-period' : 'odd-period'}">
	                <td>${item.timePeriod}</td>
	                <td>${item.warehouseName}</td>
	                <td>$${item.revenue.toLocaleString()}</td>
	                <td>${item.totalOrders}</td>
	                <td>${item.totalUnits}</td>
	                <td>${item.percentage.toFixed(1)}%</td>
	            </tr>
	        `;
	    }).join('');
	}
	function formatDate(dateString) {
	    if (!dateString) return '-';
	    const date = new Date(dateString);
	    return date.toLocaleDateString('en-US', {
	        year: 'numeric',
	        month: 'short',
	        day: 'numeric',
	        hour: '2-digit',
	        minute: '2-digit'
	    });
	}
	

	
	document.querySelectorAll('a[data-toggle="tab"]').forEach(tab => {
	    tab.addEventListener('click', function (e) {
	        const targetTab = this.getAttribute('href');
	        if (targetTab === '#warehouseTab') {
	            setTimeout(() => {
	                loadWarehouseData();
	            }, 100);
	        }
	    });
	});

	
	document.addEventListener('DOMContentLoaded', function() {
	    if (document.querySelector('#warehouseTab').classList.contains('active')) {
	        loadWarehouseData();
	    }
	});
	
