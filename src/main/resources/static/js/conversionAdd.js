document.getElementById("add-conversion-form").addEventListener("submit", function (e) {
	    e.preventDefault(); // Ngăn form submit mặc định

	    // Lấy dữ liệu từ form
	    const product_id = document.getElementById("product_id").value;
	    const to_unit_name = document.getElementById("to_unit_id").value;
	    const from_unit_name = document.getElementById("from_unit_name").value;
	    const conversion_rate = document.getElementById("conversion_rate").value;

	    // Gửi AJAX request để thêm conversion
	    fetch('/admin/addConversion', {
	        method: 'POST',
	        headers: {
	            'Content-Type': 'application/x-www-form-urlencoded'
	        },
	        body: new URLSearchParams({
	            product_id: product_id,
	            to_unit_name: to_unit_name,
	            from_unit_name: from_unit_name,
	            conversion_rate: conversion_rate
	        })
	    })
	        .then(response => {
	            if (!response.ok) {
	                return response.text().then(error => {
	                    throw new Error(error);
	                });
	            }
	            return response.json(); // Parse JSON từ phản hồi
	        })
	        .then(data => {
	            if (!data.from_unit_name || !data.to_unit_name || data.conversion_rate === undefined) {
	                throw new Error("Invalid data format from server!");
	            }
	            addNewConversionToTable(data);
	            // Thêm conversion mới vào mảng allConversions
	            allConversions.push(data);

	            // Cập nhật lại bảng và phân trang
	            renderStockTable1();
	            renderPagination1();

	            alert("Conversion added successfully!");
	        })
	        .catch(error => {
	            console.error('Error:', error);
	            alert('Failed to add conversion! Details: ' + error.message);
	        });

	});


	// Hàm thêm Conversion mới vào bảng
	function addNewConversionToTable(conversion) {
    if (!conversion) {
        console.error("Conversion data is undefined or null!");
        return;
    }
    
    const tableBody = document.getElementById('conversionTable').querySelector('tbody');
    const newRow = document.createElement('tr');
    newRow.innerHTML = `
        <td>${tableBody.children.length + 1}</td>
        <td>${conversion.from_unit_name}</td>
        <td>${conversion.to_unit_name}</td>
        <td>${conversion.conversion_rate}</td>
    `;
    tableBody.appendChild(newRow);
}
