
function confirmDelete() {
   return confirm("Are you sure you want to delete this Order?");
  }
  
  function goToDetail(id) {
      var status = document.querySelector('tr[data-id="'+id+'"] td:nth-child(4)').innerText.trim();

      if (status !== 'waiting for shipping') {
          window.location.href = '/warehouseManager/orderInWarehouseDetail?id=' + id;
      } else {
          alert("This Order is complete and cannot be edited.");
      }
  }

