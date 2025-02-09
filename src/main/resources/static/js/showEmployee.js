		//delete thông báo employee
		function confirmDeleteEmp() {
		   return confirm("Are you sure you want to delete this employee?");
		  }
		  //click vào xem chi tiết nhân viên
		  function goToDetailEmploy(id) {
		        window.location.href = '/admin/employee/showEmployeeDetail?id=' + id;
		    }
			//search text 
			function searchTable() {
			    var searchTerm = $('#searchInput').val().toLowerCase();
			    var rows = $('table tbody tr');

			    rows.each(function() {
			        var rowText = $(this).text().toLowerCase();
			        if (rowText.indexOf(searchTerm) === -1) {
			            $(this).hide();
			        } else {
			            $(this).show();
			        }
			    });
			}
			$(document).ready(function() {
			    const newCategoryRow = $(".highlighted-row");
			    if (newCategoryRow.length) {
			        $('html, body').animate({
			            scrollTop: newCategoryRow.offset().top - 100
			        }, 1000);
			    }
			});

  