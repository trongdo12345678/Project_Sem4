// Lấy các phần tử select
document.addEventListener('DOMContentLoaded', function() {
    const provinceSelect = document.getElementById('provinceId');
    const districtSelect = document.getElementById('districtSelect');
    const wardSelect = document.getElementById('wardId');

    // Gán sự kiện onchange cho select tỉnh
    provinceSelect.addEventListener('change', function() {
        loadDistricts(this.value);
    });

    // Gán sự kiện onchange cho select quận/huyện
    districtSelect.addEventListener('change', function() {
        loadWards(this.value);
    });
});
// Hàm load danh sách quận/huyện khi tỉnh được chọn
function loadDistricts(provinceId) {
    fetch(`/admin/warehouse/districts?provinceId=${provinceId}`)
        .then(response => {
            if (!response.ok) {
                throw new Error("Lỗi khi tải danh sách quận/huyện.");
            }
            return response.json();
        })
        .then(data => {
            const districtSelect = document.getElementById('districtSelect');
            districtSelect.innerHTML = '<option value="" disabled selected>Select District</option>';
            data.forEach(district => {
                const option = document.createElement('option');
                option.value = district.DistrictID;
                option.textContent = district.DistrictName;
                districtSelect.appendChild(option);
            });
        })
        .catch(error => {
            console.error('Lỗi khi tải quận/huyện:', error);
            alert('Không thể tải danh sách quận/huyện.');
        });
}
// Hàm load danh sách xã/phường khi quận/huyện được chọn
function loadWards(districtId) {
    if (!districtId) return;

    fetch(`/admin/warehouse/wards?districtId=${districtId}`)
        .then(response => {
            if (!response.ok) throw new Error('Không thể tải danh sách xã/phường');
            return response.json();
        })
        .then(wards => {
            const wardSelect = document.getElementById('wardId');
            wardSelect.innerHTML = '<option value="" disabled selected>Select Ward</option>';

            wards.forEach(ward => {
                const option = document.createElement('option');
                option.value = ward.WardCode;
                option.textContent = ward.WardName;
                wardSelect.appendChild(option);
            });
        })
        .catch(error => {
            console.error('Error:', error);
            alert('Đã xảy ra lỗi khi tải xã/phường.');
        });
}
//hàm để lưu các giá trị đã chọn vô để add address
document.addEventListener('DOMContentLoaded', function() {
    const provinceSelect = document.getElementById('provinceId');
    const districtSelect = document.getElementById('districtSelect');
    const wardSelect = document.getElementById('wardId');
    const streetAddressInput = document.getElementById('streetAddress');
    const fullAddressInput = document.getElementById('address');

    provinceSelect.addEventListener('change', updateAddress);
    districtSelect.addEventListener('change', updateAddress);
    wardSelect.addEventListener('change', updateAddress);
    streetAddressInput.addEventListener('input', updateAddress);

    function updateAddress() {
        let addressParts = [];

        // Lấy tên đường nếu có
		const streetAddress = streetAddressInput.value.trim();
		if (streetAddress) {
		    addressParts.push(streetAddress);
		}
		if (wardSelect.value) {
		    addressParts.push(wardSelect.options[wardSelect.selectedIndex].text);
		}
		if (districtSelect.value) {
		    addressParts.push(districtSelect.options[districtSelect.selectedIndex].text);
		}
		if (provinceSelect.value) {
		    addressParts.push(provinceSelect.options[provinceSelect.selectedIndex].text);
		}
        // Thêm "Vietnam" vào cuối cùng
        addressParts.push("Vietnam");
        fullAddressInput.value = removeDiacritics(addressParts.join(', '));
    }

    // Hàm để loại bỏ dấu tiếng Việt
    function removeDiacritics(str) {
        const map = {
            'a': 'áàảãạăắặằẳẵâấầẩẫậ',
            'e': 'éèẻẽẹêếềểễệ',
            'i': 'íìỉĩị',
            'o': 'óòỏõọôốồổỗộơớờởỡợ',
            'u': 'úùủũụưứừửữự',
            'y': 'ýỳỷỹỵ',
            'd': 'đ'
        };

        for (let key in map) {
            let regex = new RegExp(`[${map[key]}]`, 'g');
            str = str.replace(regex, key);
        }

        return str;
    }
});


