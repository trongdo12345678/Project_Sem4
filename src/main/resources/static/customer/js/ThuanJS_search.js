function toggleDropdown() {
    const dropdown = document.querySelector('.searchDropdown');
        dropdown.style.display = 'block'; // Hiện dropdown

}

// Thêm sự kiện để ẩn dropdown khi nhấp ra bên ngoài
document.addEventListener('click', function(event) {
    const dropdown = document.querySelector('.searchDropdown');
    const searchInput = document.querySelector('.osearch');
    if (!searchInput.contains(event.target) && !dropdown.contains(event.target)) {
        dropdown.style.display = 'none'; 
    }
});