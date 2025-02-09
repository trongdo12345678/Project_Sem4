//delete brand
function confirmDeleteBr() {
	return confirm("Are you sure you want to delete this Brand ?");
}
function goToDetail(id) {
    window.location.href = '/admin/brand/showProductFromBrand?id=' + id;
}
