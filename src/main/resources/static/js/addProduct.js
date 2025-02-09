const priceInput = document.getElementById('price');
const priceChangesInput = document.getElementById('priceChanges');

// Hàm định dạng giá trị thành định dạng tiền tệ
function formatCurrency(value) {
	return isNaN(value) ? '0.00' : parseFloat(value).toFixed(2);
}
function parseAndFormat(value) {
	value = value.replace(/[^0-9.]/g, '');
	const num = parseFloat(value);
	return isNaN(num) ? '' : num.toFixed(2);
}

// Cập nhật giá trị vào ô
function updatePriceChanges() {
	const priceValue = parseFloat(priceInput.value.replace(/[^0-9.]/g, '')) || 0;
	priceChangesInput.value = formatCurrency(priceValue);
}
function handleBlur(event) {
	let value = event.target.value;
	value = parseAndFormat(value);
	event.target.value = value;

	updatePriceChanges();
}
function handleInput(event) {
	event.target.value = event.target.value.replace(/[^0-9.]/g, '');
}

// Gán sự kiện cho ô "Price"
priceInput.addEventListener('blur', handleBlur);
priceInput.addEventListener('input', handleInput);

// Tìm kiếm thương hiệu
document.getElementById('searchBrand')?.addEventListener('input', function () {
	const filter = this.value.toLowerCase();
	const options = document.getElementById('brandId')?.options;

	if (options) {
		for (let i = 0; i < options.length; i++) {
			const optionText = options[i].text.toLowerCase();
			options[i].style.display = optionText.includes(filter) ? '' : 'none';
		}
	}
});

// Chức năng xem trước ảnh chính
function showImage(event) {
	const file = event.target.files[0];
	const preview = document.getElementById('preview');

	if (file && preview) {
		const reader = new FileReader();
		reader.onload = function (e) {
			preview.src = e.target.result;
			preview.style.display = 'block';
		};
		reader.readAsDataURL(file);
	} else if (preview) {
		preview.style.display = 'none';
	}
}

// Gán ngày hiện tại vào ô "Date Start" (nếu tồn tại)
const dateStartInput = document.getElementById("dateStart");
if (dateStartInput) {
	dateStartInput.value = new Date().toISOString().split('T')[0];
}

// Khởi tạo CKEditor 
if (typeof CKEDITOR !== 'undefined') {
	CKEDITOR.replace('description');
}
