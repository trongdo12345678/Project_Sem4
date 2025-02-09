// Biến toàn cục
let inventoryData = [];
let paginatedData = [];
let currentPage = 1;
const pageSize = 6;
let lowStockChart, inventoryChart;

const lowStockApiUrl = "/warehouseManager/lowStockApi";
const inventoryApiUrl = '/warehouseManager/inventory-stats';

// Hàm cập nhật biểu đồ "lowStockChart"
function updateLowStockChart(minQuantity) {
	const apiUrl = `${lowStockApiUrl}?minQuantity=${minQuantity || ''}`;
	fetchData(apiUrl, (data) => {
		const labels = data.map(item => item.productName);
		const quantities = data.map(item => item.totalQuantity);
		const backgroundColors = data.map(item =>
			item.totalQuantity < minQuantity ? 'rgba(255, 99, 132, 0.5)' : 'rgba(75, 192, 192, 0.5)'
		);

		const lowStockChartElement = document.getElementById('lowStockChart');
		if (lowStockChart) {
			lowStockChart.data.labels = labels;
			lowStockChart.data.datasets[0].data = quantities;
			lowStockChart.data.datasets[0].backgroundColor = backgroundColors;
			lowStockChart.update();
		} else {
			lowStockChart = new Chart(lowStockChartElement, {
				type: 'bar',
				data: {
					labels: labels,
					datasets: [{
						label: 'Quantity',
						data: quantities,
						backgroundColor: backgroundColors,
						borderColor: 'rgba(75, 192, 192, 1)',
						borderWidth: 1
					}]
				},
				options: {
					responsive: true,
					plugins: {
						legend: { position: 'top' },
						title: { display: true, text: 'Low Inventory Product Statistics' }
					},
					scales: {
						y: {
							beginAtZero: true,
							title: { display: true, text: 'Quantity' }
						},
						x: {
							title: { display: true, text: 'Product Name' }
						}
					}
				}

			});
		}
	});
}

window.addEventListener('resize', () => {
	if (lowStockChart) {
		lowStockChart.resize(); // Cập nhật kích thước biểu đồ theo container
	}
});


// Hàm cập nhật biểu đồ "inventoryChart"
function updateInventoryChart(pageData) {
	const labels = pageData.map(item => `${item.ProductName} (${item.ImportDate})`);
	const quantities = pageData.map(item => item.StatusProduct === 'in stock' ? item.StockQuantity : 0);
	const backgroundColors = pageData.map(item =>
		item.StatusProduct === 'in stock' ? 'rgba(75, 192, 192, 0.7)' : 'rgba(255, 99, 132, 0.7)'
	);
	const messages = pageData.map(item =>
		item.StatusProduct === 'out of stock' ? 'Out of Stock' : ''
	);

	const inventoryChartElement = document.getElementById('inventoryChart');
	if (inventoryChart) {
		inventoryChart.data.labels = labels;
		inventoryChart.data.datasets[0].data = quantities;
		inventoryChart.data.datasets[0].backgroundColor = backgroundColors;
		inventoryChart.options.plugins.customText.messages = messages; // Cập nhật thông điệp
		inventoryChart.update();
	} else {
		inventoryChart = new Chart(inventoryChartElement, {
			type: 'bar',
			data: {
				labels: labels,
				datasets: [{
					label: 'Stock Quantity',
					data: quantities,
					backgroundColor: backgroundColors,
					borderColor: 'rgba(75, 192, 192, 1)',
					borderWidth: 1
				}]
			},
			options: {
				responsive: true,
				maintainAspectRatio: false,
				plugins: {
					legend: { position: 'top' },
					title: { display: true, text: 'Inventory Statistics: In Stock vs Out of Stock' },
					customText: { messages: messages } // Khai báo plugin và thông điệp
				},
				scales: {
					y: {
						beginAtZero: true,
						title: { display: true, text: 'Quantity' }
					},
					x: {
						title: { display: true, text: 'Product (Date)' }
					}
				}
			},
			plugins: [
				{
					id: 'customText',
					beforeDraw(chart) {
						const ctx = chart.ctx;
						const chartArea = chart.chartArea;
						const meta = chart.getDatasetMeta(0);
						const messages = chart.options.plugins.customText.messages;

						ctx.save();
						meta.data.forEach((bar, index) => {
							const message = messages[index];
							if (message) {
								const x = bar.x;
								const y = chartArea.bottom - 20;

								// Hiệu ứng hộp nền
								const chartWidth = chartArea.right - chartArea.left;
								const boxPadding = chartWidth * 0.025; 
								const textWidth = ctx.measureText(message).width;
								const boxWidth = textWidth + boxPadding * 2;
								const boxHeight = chartWidth * 0.03;

								ctx.fillStyle = 'rgba(255, 99, 132, 0.8)';
								ctx.fillRect(
									x - boxWidth / 2,
									y - boxHeight / 2,
									boxWidth,
									boxHeight
								);

								ctx.strokeStyle = 'rgba(255, 99, 132, 1)';
								ctx.lineWidth = 2;
								ctx.strokeRect(
									x - boxWidth / 2,
									y - boxHeight / 2,
									boxWidth,
									boxHeight
								);

								// Hiển thị thông điệp
								ctx.textAlign = 'center';
								ctx.textBaseline = 'middle';
								ctx.fillStyle = 'white';
								ctx.font = 'bold 14px Arial';
								ctx.fillText(message, x, y);
							}
						});
						ctx.restore();
					}
				}
			]
		});
	}
}

//aaa
// Áp dụng bộ lọc theo ngày và cập nhật biểu đồ
function applyFilter(startDate, endDate) {
	const startDateInput = startDate || document.getElementById('startDate').value;
	const endDateInput = endDate || document.getElementById('endDate').value;

	if (!startDateInput || !endDateInput) {
		alert("Vui lòng chọn đầy đủ ngày bắt đầu và ngày kết thúc.");
		return;
	}


	// Gửi yêu cầu AJAX để lấy dữ liệu đã lọc theo ngày
	fetch(`${inventoryApiUrl}?startDate=${startDateInput}&endDate=${endDateInput}`)
		.then(response => {
			if (!response.ok) {
				throw new Error("API call failed: " + response.status);
			}
			return response.json();
		})
		.then(data => {
			if (data.length === 0) {
				alert("Không có dữ liệu trong khoảng thời gian này.");
			}
			filteredData = data; // Cập nhật dữ liệu đã lọc vào filteredData
			currentPage = 1; // Đặt lại trang về trang đầu khi áp dụng bộ lọc
			renderPage(currentPage); // Cập nhật lại trang với dữ liệu đã lọc
		})
		.catch(error => {
			console.error("Không lấy được dữ liệu:", error);
			alert("Có lỗi xảy ra khi tải dữ liệu.");
		});
}
// Gắn sự kiện thay đổi cho các trường ngày
function initDateFilter() {
	document.getElementById('startDate').addEventListener('change', function() {
		const startDate = this.value;
		const endDate = document.getElementById('endDate').value;
		applyFilter(startDate, endDate);
	});

	document.getElementById('endDate').addEventListener('change', function() {
		const startDate = document.getElementById('startDate').value;
		const endDate = this.value;
		applyFilter(startDate, endDate);
	});

	const defaultStartDate = '2020-01-01';
	const defaultEndDate = new Date().toISOString().split('T')[0];
	applyFilter(defaultStartDate, defaultEndDate);
}

// Khởi tạo sự kiện
document.addEventListener("DOMContentLoaded", function() {
	initDateFilter();
});


//bbb

// Gọi API và xử lý dữ liệu
function fetchData(apiUrl, callback) {
	fetch(apiUrl)
		.then(response => {
			if (!response.ok) throw new Error("API call failed: " + response.status);
			return response.json();
		})
		.then(data => callback(data))
		.catch(error => {
			console.error("Error fetching data: ", error);
		});
}

// Tải dữ liệu inventory từ API và hiển thị biểu đồ đầu tiên
function loadData() {
	fetchData(inventoryApiUrl, (data) => {
		inventoryData = data;
		paginatedData = inventoryData;
		renderPage(currentPage);
	});
}

// Hiển thị trang hiện tại và cập nhật biểu đồ
function renderPage(page) {
	const startIndex = (page - 1) * pageSize;
	const endIndex = startIndex + pageSize;
	const pageData = filteredData.slice(startIndex, endIndex); // Sử dụng filteredData thay vì inventoryData
	updateInventoryChart(pageData); // Cập nhật biểu đồ với dữ liệu đã lọc
	updatePaginationControls(); // Cập nhật các nút phân trang
}

// Cập nhật các nút phân trang
function updatePaginationControls() {
	const paginationControls = document.getElementById('paginationControls');
	const totalPages = Math.ceil(filteredData.length / pageSize); // Sử dụng filteredData
	paginationControls.innerHTML = '';

	// Nút "<"
	const prevButton = document.createElement('button');
	prevButton.textContent = '<';
	prevButton.disabled = currentPage === 1;
	prevButton.addEventListener('click', () => {
		currentPage--;
		renderPage(currentPage);
	});
	paginationControls.appendChild(prevButton);

	// Nút số trang
	const maxVisiblePages = 5;
	let startPage = Math.max(1, currentPage - Math.floor(maxVisiblePages / 2));
	let endPage = Math.min(totalPages, startPage + maxVisiblePages - 1);

	if (endPage - startPage + 1 < maxVisiblePages) {
		startPage = Math.max(1, endPage - maxVisiblePages + 1);
	}

	for (let i = startPage; i <= endPage; i++) {
		const pageButton = document.createElement('button');
		pageButton.textContent = i;
		pageButton.className = i === currentPage ? 'active' : '';
		pageButton.addEventListener('click', () => {
			currentPage = i;
			renderPage(currentPage);
		});
		paginationControls.appendChild(pageButton);
	}

	// Nút ">"
	const nextButton = document.createElement('button');
	nextButton.textContent = '>';
	nextButton.disabled = currentPage === totalPages;
	nextButton.addEventListener('click', () => {
		currentPage++;
		renderPage(currentPage);
	});
	paginationControls.appendChild(nextButton);
}

// Sự kiện DOM
document.addEventListener("DOMContentLoaded", function() {
	document.getElementById('updateChart').addEventListener('click', function() {
		const minQuantity = document.getElementById('minQuantity').value;
		updateLowStockChart(minQuantity);
	});

	updateLowStockChart(null);
	loadData();
});
