//randum tên cho hko
$(document).ready(function() {
	$.ajax({
		url: '/warehouseManager/warehouseReceipt/generateReceiptName',
		type: 'GET',
		success: function(data) {
			console.log("Received data:", data);
			$('#name').val(data);
		},
		error: function() {
			console.error("Failed to generate receipt name.");
		}
	});
});
//tìm kiếm product trong select
function filterProducts(inputElement) {
	const filter = inputElement.value.toLowerCase();
	const detailGroup = inputElement.closest('.detail-group');
	const select = detailGroup.querySelector('.product_id');

	const options = select.options;

	let found = false;
	for (let i = 1; i < options.length; i++) {
		const option = options[i];
		const txtValue = option.textContent || option.innerText;
		if (txtValue.toLowerCase().includes(filter)) {
			option.style.display = "";
			found = true;
		} else {
			option.style.display = "none";
		}
	}

	const noResultOption = select.querySelector('option[value="noresult"]');
	if (!found && !noResultOption) {
		const noResultOption = document.createElement('option');
		noResultOption.value = "noresult";
		noResultOption.textContent = 'No matching products';
		noResultOption.disabled = true;
		select.appendChild(noResultOption);
	} else if (found && noResultOption) {
		select.removeChild(noResultOption);
	}
}
// Giới hạn cho wh_price
function formatPriceInput(inputElement) {
	inputElement.addEventListener('blur', function(e) {
		let input = e.target.value;
		input = input.replace(/[^0-9.]/g, '');

		let value = parseFloat(input);
		if (!isNaN(value)) {
			if (value > 100000) {
				value = 100000;
				alert('Maximum value allowed is 100,000.');
			}
			e.target.value = value.toFixed(2);
		} else {
			e.target.value = '';
		}
	});

	inputElement.addEventListener('input', function(e) {
		e.target.value = e.target.value.replace(/[^0-9.]/g, '');

		const value = parseFloat(e.target.value);
		if (value > 100000) {
			e.target.value = '100000';
			alert('Maximum value allowed is 100,000.');
		}
	});
}
document.addEventListener('DOMContentLoaded', function() {
	const whPriceInput = document.getElementById('wh_price');
	const shippingFeeInput = document.getElementById('shippingFee');
	const otherFeeInput = document.getElementById('otherFee');

	if (whPriceInput) {
		formatPriceInput(whPriceInput);
	}
	if (shippingFeeInput) {
		formatPriceInput(shippingFeeInput);
	}
	if (otherFeeInput) {
		formatPriceInput(otherFeeInput);
	}
});

document.querySelector('form').addEventListener('submit', function(event) {
	var otherFee = document.getElementById('otherFee').value;

	// Nếu không nhập gì, gán giá trị mặc định là 0
	if (otherFee.trim() === '') {
		document.getElementById('otherFee').value = 0;
	}
});
// Thêm nhóm chi tiết mới và xử lý sự kiện chọn sản phẩm trong nhóm đó
function addDetailGroup() {
    const detailsContainer = document.getElementById('detailsContainer');

    let receiptCounter = parseInt(detailsContainer.getAttribute('data-receipt-counter')) || 0;
    receiptCounter++;

    const originalReceiptGroup = detailsContainer.querySelector('.detail-group').cloneNode(true);

    const existingTitle = originalReceiptGroup.querySelector('.receipt-title');
    if (existingTitle) {
        existingTitle.innerText = `Warehouse Receipt ${receiptCounter}`;
    } else {
        const newTitle = document.createElement('h3');
        newTitle.classList.add('receipt-title');
        newTitle.innerText = `Warehouse Receipt ${receiptCounter}`;
        originalReceiptGroup.insertBefore(newTitle, originalReceiptGroup.firstChild);
    }

    const inputs = originalReceiptGroup.querySelectorAll('input, select');
    inputs.forEach(input => {
        input.value = '';
        if (input.tagName === 'SELECT') {
            input.selectedIndex = 0;
        }
        if (input.name === 'statusDetails') {
            input.value = 'Active';
        }
    });

    const deleteButton = document.createElement('button');
    deleteButton.type = 'button';
    deleteButton.classList.add('btn', 'btn-danger', 'delete-group-button');
    deleteButton.innerText = 'Remove';
    deleteButton.onclick = function () {
        detailsContainer.removeChild(originalReceiptGroup.previousElementSibling);
        detailsContainer.removeChild(originalReceiptGroup);
    };

    const buttonContainer = document.createElement('div');
    buttonContainer.classList.add('button-container');
    buttonContainer.appendChild(deleteButton);

    originalReceiptGroup.appendChild(buttonContainer);

    const separator = document.createElement('hr');
    separator.classList.add('separator');
    detailsContainer.appendChild(separator);
    detailsContainer.appendChild(originalReceiptGroup);
    detailsContainer.setAttribute('data-receipt-counter', receiptCounter);
}



// Hàm thêm sự kiện định dạng tiền
function addWarehousePriceEvents(inputElement) {
	inputElement.addEventListener('blur', function(e) {
		let input = e.target.value;
		input = input.replace(/[^0-9.]/g, '');
		let value = parseFloat(input);

		if (!isNaN(value)) {
			if (value > 100000) {
				value = 100000;
				alert('Maximum warehouse price allowed is 100,000.');
			}
			e.target.value = value.toFixed(2);
		} else {
			e.target.value = '';
		}
	});


	inputElement.addEventListener('input', function(e) {
		e.target.value = e.target.value.replace(/[^0-9.]/g, '');
		const value = parseFloat(e.target.value);
		if (value > 100000) {
			e.target.value = '100000';
			alert('Maximum warehouse price allowed is 100,000.');
		}
	});
}

document.addEventListener('DOMContentLoaded', function() {
	const warehousePriceInputs = document.querySelectorAll('input[name="wh_price"]');
	warehousePriceInputs.forEach(input => addWarehousePriceEvents(input));
});

// Cập nhật fetchConversionId để chọn conversion tương ứng với product
function fetchConversionId(selectElement) {
	var productId = selectElement.value;
	if (productId) {
		fetch('/warehouseManager/warehouseReceipt/getConversions?id=' + productId)
			.then(response => response.json())
			.then(data => {
				console.log(data)
				var conversionSelect = selectElement.closest('.detail-group').querySelector('.conversion_id');
				conversionSelect.innerHTML = '<option value="" disabled selected>Select Conversion</option>'; // Xóa các option cũ

				data.forEach(conversion => {
					var option = document.createElement('option');
					option.value = conversion.id;
					option.text = conversion.fromUnitName + '->' + conversion.toUnitName + '(' + conversion.conversion_rate + ')';
					conversionSelect.appendChild(option);
				});
			})
			.catch(error => {
				console.error('Error fetching conversions:', error);
			});
	} else {
		var conversionSelect = selectElement.closest('.detail-group').querySelector('.conversion_id');
		conversionSelect.innerHTML = '<option value="" disabled selected>Select Conversion</option>';
	}
}








