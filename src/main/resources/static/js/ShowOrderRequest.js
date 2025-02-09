function loadPage(pageNumber) {
    $.ajax({
        type: "GET",
        url: "/businessManager/showOrderRequest",
        data: {
            cp: pageNumber
        },
        success: function(response) {
            $('#pageContent').html(response); 

            updatePagination(response.pv);
        },
        error: function(e) {
            console.log("Error: ", e);
        }
    });
}

function goToDetail(id) {
      window.location.href = '/businessManager/showOrderDetailForm?id=' + id;
  }

function confirmDelete() {
   return confirm("Are you sure you want to delete this OrderRequest?");
  }

  function searchReleaseNotes() {
      const query = document.getElementById("searchInput").value;

      fetch(`/businessManager/searchOrderRequest?query=${encodeURIComponent(query)}`)
          .then(response => response.json())
          .then(data => {
              const tableBody = document.getElementById("OrderRequestTable");
              tableBody.innerHTML = ""; 

              if (data.length === 0) {
                  const noDataRow = document.createElement("tr");
                  noDataRow.className = "text-center";
                  noDataRow.innerHTML = `<td colspan="5">No order requests found</td>`;
                  tableBody.appendChild(noDataRow);
              } else {
                  data.forEach(item => {
                      const row = document.createElement("tr");
                      row.className = "text-center"; 
                      row.innerHTML = `
                          <td>${item.id}</td>
                          <td onclick="goToDetail(${item.id})">${item.name}</td>
                          <td onclick="goToDetail(${item.id})">${new Date(item.date).toLocaleDateString()}</td>
                          <td onclick="goToDetail(${item.id})">${item.status}</td>
                          <td>
                              <a href="deleteOrderRequest?id=${item.id}" onclick="return confirmDelete();" class="btn btn-danger">Delete</a>
                          </td>
                      `;
                      tableBody.appendChild(row);
                  });
              }
          })
          .catch(error => {
              console.error('Error fetching search results:', error);
          });
  }
