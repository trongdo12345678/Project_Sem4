const priceInput = document.getElementById('price');
const newPriceInput = document.getElementById('newPrice');

function formatCurrency(value) {
    return value.toFixed(2);
}

function parseAndFormat(value) {
    value = value.replace(/[^0-9.]/g, ''); // Xóa ký tự không hợp lệ
    const num = parseFloat(value);
    return isNaN(num) ? '' : num.toFixed(2); // Hiển thị 2 chữ số sau dấu phẩy
}

function updatePriceChanges() {
    const priceValue = parseFloat(priceInput.value.replace(/[^0-9.]/g, '')) || 0;
    newPriceInput.value = formatCurrency(priceValue); // Cập nhật giá trị cho ô "New Price"
}

// Xử lý khi người dùng rời khỏi ô nhập (blur)
function handleBlur(event) {
    let value = event.target.value;
    value = parseAndFormat(value); // Định dạng giá trị
    event.target.value = value; // Cập nhật lại giá trị ô nhập

    // Cập nhật giá trị cho "New Price"
    updatePriceChanges();
}

// Gán sự kiện cho ô "Price"
priceInput.addEventListener('blur', handleBlur);

// Khi nhập liệu, chỉ cho phép số và dấu chấm
function handleInput(event) {
    event.target.value = event.target.value.replace(/[^0-9.]/g, ''); // Xóa ký tự không hợp lệ
}

// Gán sự kiện cho ô "Price"
priceInput.addEventListener('input', handleInput);

// Chức năng xem trước ảnh chính

function showImage(event) {
    var reader = new FileReader();
    reader.onload = function() {
        var output = document.getElementById('preview');
        output.style.display = 'block'; // Hiển thị ảnh preview
        output.src = reader.result; // Gán giá trị ảnh từ FileReader
    };
    reader.readAsDataURL(event.target.files[0]);
}

document.addEventListener('DOMContentLoaded', function() {
    if (typeof CKEDITOR !== 'undefined') {
        CKEDITOR.replace('description'); // Phải khớp với ID của textarea
    } else {
        console.error("CKEditor không được tải.");
    }
});

// không được nhập số âm
document.querySelectorAll('input[type="number"]').forEach(input => {
    input.addEventListener('keydown', function(event) {
        if (event.key === '-' || event.key === 'e' || event.key === 'E') {
            event.preventDefault();
        }
    });
});

