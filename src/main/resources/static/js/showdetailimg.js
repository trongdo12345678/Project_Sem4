// Chức năng xem trước ảnh bổ sung
document.addEventListener("DOMContentLoaded", function() {
	const imageInput = document.getElementById("additionalImages");
	const imagePreviewContainer = document.getElementById("imagePreviewContainer");

	// Mảng để lưu trữ các file đã chọn
	let selectedFiles = [];

	imageInput.addEventListener("change", function(event) {
		const files = event.target.files; // Lấy danh sách file

		// Thêm các file mới vào mảng selectedFiles
		selectedFiles = Array.from(files);

		if (!selectedFiles.length) return; // Không có file nào được chọn

		// Xóa các preview hiện tại trước khi thêm mới
		imagePreviewContainer.innerHTML = '';

		selectedFiles.forEach((file) => {
			if (!file.type.startsWith("image/")) {
				alert("Please select only image files.");
				return;
			}

			const fileReader = new FileReader();

			fileReader.onload = function(e) {
				// Tạo div cho xem trước ảnh
				const previewDiv = document.createElement("div");
				previewDiv.className = "image-preview";

				// Tạo thẻ img để hiển thị ảnh
				const img = document.createElement("img");
				img.src = e.target.result;

				// Tạo nút xóa
				const removeBtn = document.createElement("button");
				removeBtn.className = "remove-btn";
				removeBtn.textContent = "×";
				removeBtn.addEventListener("click", function() {
					const index = selectedFiles.indexOf(file);
					if (index !== -1) {
						selectedFiles.splice(index, 1); // Xóa file khỏi mảng
						imageInput.files = createFileList(selectedFiles); // Cập nhật lại input
					}
					previewDiv.remove(); // Xóa xem trước khi nhấn nút
				});

				// Thêm img và nút xóa vào previewDiv
				previewDiv.appendChild(img);
				previewDiv.appendChild(removeBtn);

				// Thêm previewDiv vào container
				imagePreviewContainer.appendChild(previewDiv);
			};

			fileReader.readAsDataURL(file); // Đọc file dưới dạng Data URL
		});
	});

	// Hàm tạo FileList mới từ mảng các file
	function createFileList(files) {
		const dataTransfer = new DataTransfer();
		files.forEach(file => dataTransfer.items.add(file));
		return dataTransfer.files; // Trả về FileList mới
	}
});
//hàm nhân thêm Specification Product
document.getElementById('addSpecBtn').addEventListener('click', function () {
	const container = document.getElementById('specContainer');
	const rowIndex = container.children.length + 1;

	const newCardBody = document.createElement('div');
	newCardBody.className = 'card-body';

	const inputRow = document.createElement('div');
	inputRow.className = 'row';

	const nameDiv = document.createElement('div');
	nameDiv.className = 'form-group col-6';
	const nameLabel = document.createElement('label');
	nameLabel.setAttribute('for', `specNames-${rowIndex}`);
	nameLabel.innerText = `Specification Name ${rowIndex}`;
	const nameInput = document.createElement('input');
	nameInput.type = 'text';
	nameInput.id = `specNames-${rowIndex}`;
	nameInput.name = 'specNames[]';
	nameInput.className = 'form-control';
	nameInput.required = true;

	nameDiv.appendChild(nameLabel);
	nameDiv.appendChild(nameInput);

	const descDiv = document.createElement('div');
	descDiv.className = 'form-group col-6';
	const descLabel = document.createElement('label');
	descLabel.setAttribute('for', `specDescriptions-${rowIndex}`);
	descLabel.innerText = `Specification Description ${rowIndex}`;
	const descInput = document.createElement('input');
	descInput.type = 'text';
	descInput.id = `specDescriptions-${rowIndex}`;
	descInput.name = 'specDescriptions[]';
	descInput.className = 'form-control';
	descInput.required = true;

	descDiv.appendChild(descLabel);
	descDiv.appendChild(descInput);

	inputRow.appendChild(nameDiv);
	inputRow.appendChild(descDiv);

	const buttonDiv = document.createElement('div');
	buttonDiv.className = 'd-flex justify-content-end mt-2';
	const removeButton = document.createElement('button');
	removeButton.type = 'button';
	removeButton.className = 'btn btn-danger removeSpecBtn';
	removeButton.innerText = 'Remove';

	removeButton.addEventListener('click', function () {
		container.removeChild(newCardBody);
	});

	buttonDiv.appendChild(removeButton);

	newCardBody.appendChild(inputRow);
	newCardBody.appendChild(buttonDiv);

	container.appendChild(newCardBody);
});

// không được nhập số âm
document.querySelectorAll('input[type="number"]').forEach(input => {
	input.addEventListener('keydown', function(event) {
		// Chặn phím '-' hoặc các phím không hợp lệ
		if (event.key === '-' || event.key === 'e' || event.key === 'E') {
			event.preventDefault(); // Ngăn không cho nhập
		}
	});
});
function validateImageCount() {
        const imageInput = document.getElementById('additionalImages');
        const fileCount = imageInput.files.length;

        if (fileCount > 10) {
            document.getElementById('imageError').style.display = 'block';
            document.getElementById('submitBtn').disabled = true;
            return false;
        } else {
            document.getElementById('imageError').style.display = 'none';
            document.getElementById('submitBtn').disabled = false;
            return true; 
        }
    }

    document.getElementById('additionalImages').addEventListener('change', function () {
        validateImageCount();
    });
