
function confirmDelete() {
   return confirm("Are you sure you want to delete this Request?");
  }
  
  function goToDetail(id) {
      // Tìm kiếm dòng chứa ID và lấy giá trị của statusRequest từ ô status
      var status = document.querySelector('tr[data-id="'+id+'"] td:nth-child(4)').innerText.trim();

      if (status !== 'waiting for shipping') {
          window.location.href = '/warehouseManager/requestInWarehouseDetail?id=' + id;
      } else {
          alert("This request is complete and cannot be edited.");
      }
  }

