function loadPage(pageNumber) {
    $.ajax({
        type: "GET",
        url: "/admin/conversions",
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


function confirmDelete() {
   return confirm("Are you sure you want to delete this conversion?");
  }
