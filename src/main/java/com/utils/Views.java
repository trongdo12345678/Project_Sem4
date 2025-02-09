package com.utils;

public final class Views {

	// login.employee
	public static String EMPLOYEE_LOGIN = "admin/employee/EmpLogin";

	// 1.UNIT
	public static String SHOW_UNIT = "admin/unit/showUnit";
	public static String ADD_UNIT = "admin/unit/addUnit";
	public static String UPDATE_UNIT = "admin/unit/updateUnit";

	public static String TBL_UNIT = "Unit";
	public static String COL_UNIT_ID = "Id";
	public static String COL_UNIT_NAME = "Name";

	// 2.Conversion
	public static String ADD_CONVERSION = "admin/conversion/addConversion";
	public static String UPDATE_CONVERSION = "admin/conversion/updateConversion";
	public static String SHOW_CONVERSION = "admin/conversion/ShowConversion";

	public static String TBL_CONVERSION = "Conversion";
	public static String COL_CONVERSION_ID = "Id";
	public static String COL_CONVERSION_PRODUCT_ID = "Product_Id";
	public static String COL_CONVERSION_FROM_UNIT_ID = "From_unit_id";
	public static String COL_CONVERSION_TO_UNIT_ID = "To_unit_id";
	public static String COL_CONVERSION_RATE = "Conversion_rate";

	// 3.BRAND
	public static String BRAND_SHOWBRAND = "admin/brand/showBrand";
	public static String BRAND_SHOWADDBRAND = "admin/brand/showAddBrand";
	public static String BRAND_SHOWUPDATEBRAND = "admin/brand/showUpdateBrand";

	public static String TBL_BRAND = "Brand";
	public static String COL_BRAND_ID = "Id";
	public static String COL_BRAND_NAME = "Name";

	// 4.PRODUCT
	public static String PRODUCT_SHOWPRODUCT = "admin/product/showProduct";
	public static String PRODUCT_SHOWADDPRODUCT = "admin/product/showAddProduct";
	public static String PRODUCT_SHOWUPDATEPRODUCT = "admin/product/showUpdateProduct";
	public static String PRODUCT_SHOWPRODUCTDETAIL = "admin/product/showProductDetail";

	public static String TBL_PRODUCT = "Product";
	public static String COL_PRODUCT_ID = "Id";
	public static String COL_PRODUCT_NAME = "Product_name";
	public static String COL_PRODUCT_CATE_ID = "Cate_Id";
	public static String COL_PRODUCT_BRAND_ID = "Brand_Id";
	public static String COL_PRODUCT_UNIT_ID = "Unit_id";
	public static String COL_PRODUCT_CONVERSION_ID = "Id_Conversion";
	public static String COL_PRODUCT_PRICE = "Price";
	public static String COL_PRODUCT_IMG = "Img";
	public static String COL_PRODUCT_STATUS = "Status";
	public static String COL_PRODUCT_DESCIPTION = "Description";
	public static String COL_PRODUCT_WARRANTY_PERIOD = "Warranty_period";
	public static String COL_PRODUCT_LENGTH = "Length";
	public static String COL_PRODUCT_WIDTH = "Width";
	public static String COL_PRODUCT_HEIGHT = "Height";
	public static String COL_PRODUCT_WELGHT = "Weight";
	// 5.PRODUCT_CATEGORY
	public static String CATEGORY_SHOWCATEGORY = "admin/category/showCategory";
	public static String CATEGORY_SHOWADDCATEGORY = "admin/category/showAddCategory";
	public static String CATEGORY_SHOWUPDATECATEGORY = "admin/category/showUpdateCategory";

	public static String TBL_CATEGORY = "Product_category";
	public static String COL_CATEGORY_ID = "Id";
	public static String COL_CATEGORY_NAME = "Cate_name";

	// 6.PRODUCT_PRICE_CHANGE
	public static String TBL_PRODUCT_PRICE_CHANGE = "Product_price_change";
	public static String COL_PRICE_CHANGE_ID = "Id";
	public static String COL_PRICE_CHANGE_PRODUCT_ID = "Product_Id";
	public static String COL_PRICE_CHANGE_PRICE = "Price";
	public static String COL_PRICE_CHANGE_DATE_START = "Date_start";
	public static String COL_PRICE_CHANGE_DATE_END = "Date_end";
	
	//7.STOCK
	public static String SHOW_STOCK = "warehouseManager/stock/showListStock";
	public static String SHOW_STOCK_DETAIL = "warehouseManager/stock/showListStockDetail";
	public static String SHOW_STOCK_CHAR = "warehouseManager/stock/showStockChar";
	public static String SHOW_STOCK_LOW = "warehouseManager/stock/showStockLow";


	public static String TBL_STOCK = "Stock";
	public static String COL_STOCK_ID = "Id";
	public static String COL_STOCK_PRODUCT_ID = "Id_product";
	public static String COL_STOCK_QUANTITY = "Quantity";
	public static String COL_STOCK_STATUS = "Status";
	public static String COL_STOCK_WARERCDT_ID = "Wh_rc_dt_id";

	// 8.WAREHOUSE_RECEIPT_DETAIL
		public static String TBL_WAREHOUSE_RECEIPT_DETAIL = "Warehouse_receipt_detail";
		public static String COL_WAREHOUSE_RECEIPT_DETAIL_ID = "Id";
		public static String COL_DETAIL_WAREHOUSE_RECEIPT_ID = "Wh_receiptId";
		public static String COL_WAREHOUSE_RECEIPT_DETAIL_QUANTITY = "Quantity";
		public static String COL_WAREHOUSE_RECEIPT_DETAIL_WH_PRICE = "Wh_price";
		public static String COL_WAREHOUSE_RECEIPT_DETAIL_PRODUCT_ID = "Product_Id";
		public static String COL_WAREHOUSE_RECEIPT_DETAIL_CONVERSION = "Conversion_Id";

	// 9.WAREHOUSE_RECEIPT
	public static String COL_WAREHOUSE_RECEIPT_DETAIL_CONVERSION_RATE = "Conversion_rate";
	public static String COL_WAREHOUSE_RECEIPT_DETAILS_STATUS = "Status";
	
	//10.WAREHOUSE_RECEIPT 
	public static String ADD_WAREHOUSE_RECEIPT = "warehouseManager/warehouseReceipt/showAddWhReceipt";

	public static String SHOW_WAREHOUSE_RECEIPT = "warehouseManager/warehouseReceipt/showWhReceipt";
	public static String SHOW_WAREHOUSE_RECEIPT_DETAILS = "warehouseManager/warehouseReceipt/showWhReceiptDetail";
	public static String UPDATE_WAREHOUSE_RECEIPT = "warehouseManager/warehouseReceipt/showUpdateWhReceipt";
	public static String UPDATE_WAREHOUSE_RECEIPT_DETAIL = "warehouseManager/warehouseReceipt/showUpdateWhDetail";

	public static String TBL_WAREHOUSE_RECEIPT = "Warehouse_receipt";
	public static String COL_WAREHOUSE_RECEIPT_ID = "Id";
	public static String COL_WAREHOUSE_RECEIPT_NAME = "Name";
	public static String COL_WAREHOUSE_RECEIPT_IDWH = "Wh_Id";
	public static String COL_WAREHOUSE_RECEIPT_STATUS = "Status";
	public static String COL_WAREHOUSE_RECEIPT_DATE = "Date";
	public static String COL_WAREHOUSE_RECEIPT_EMP_ID = "Employee_Id";
	public static String COL_WAREHOUSE_RECEIPT_SHIPPINGFEE = "Shipping_fee";
	public static String COL_WAREHOUSE_RECEIPT_OTHERFEE = "Other_fee";
	public static String COL_WAREHOUSE_RECEIPT_TOTALFEE = "Total_fee";

	// 10.WAREHOUSE
	public static String WAREHOUSE_SHOWWAREHOUSE = "admin/warehouse/showWarehouse";
	public static String WAREHOUSE_SHOWADDWAREHOUSE = "admin/warehouse/showAddWarehouse";
	public static String WAREHOUSE_SHOWUPDATEWAREHOUSE = "admin/warehouse/showUpdateWarehouse";
	public static String WAREHOUSE_SHOWWAREHOUSEDETAILS = "admin/warehouse/showWarehouseDetails";

	public static String TBL_WAREHOUSE = "Warehouse";
	public static String COL_WAREHOUSE_ID = "Id";
	public static String COL_WAREHOUSE_NAME = "Name";
	public static String COL_WAREHOUSE_ADDRESS = "Address";
	public static String COL_WAREHOUSE_TYPE_WAREHOUSE_ID = "Wh_type_Id";
	public static String COL_WAREHOUSE_PROVINCE_ID = "Province_Id";
	public static String COL_WAREHOUSE_GHN_STORE_ID = "ghn_store_id";
	public static String COL_WAREHOUSE_DISTRICT_ID = "District_Id";
	public static String COL_WAREHOUSE_WARD_ID = "Ward_Id";
	public static String COL_WAREHOUSE_LAT = "lat";
	public static String COL_WAREHOUSE_LNG = "lng";
	// 11.WAREHOUSE_TYPE
	public static String WAREHOUSETYPE_SHOWWHTYPE = "admin/warehouseType/showWhType";
	public static String WAREHOUSETYPE_SHOWADDWHTYPE = "admin/warehouseType/showAddWhType";
	public static String WAREHOUSETYPE_SHOWUPDATEWHTYPE = "admin/warehouseType/showUpdateWhType";

	public static String TBL_WAREHOUSE_TYPE = "Warehouse_type";
	public static String COL_WAREHOUSE_TYPE_ID = "Id";
	public static String COL_WAREHOUSE_TYPE_NAME = "Name";

	// 12.Warehouse_rn_detail
	public static String SHOW_WAREHOUSE_RELEASENOTE_DETAIL = "warehouseManager/releasenote/WarehouseReleasenoteDetail";
	public static String SHOW_REQUEST_WAREHOUSE_RELEASENOTE_DETAIL = "warehouseManager/orderRequest/showOrderRequestDetail";
	public static String SHOW_ORDER_DETAIL = "warehouseManager/orderInfor/OrderDetail";

	public static String TBL_WAREHOUSE_RN_DETAIL = "Warehouse_rn_detail";
	public static String COL_WAREHOUSE_RN_DETAIL_ID = "id";
	public static String COL_WAREHOUSE_RN_DETAIL_PRODUCTID = "Id_product";
	public static String COL_WAREHOUSE_RNOTE_ID = "Wgrn_Id";
	public static String COL_WAREHOUSE_RN_DETAIL_STATUS = "Status";
	public static String COL_WAREHOUSE_RN_DETAIL_STOCK_ID = "Stock_Id";
	public static String COL_WAREHOUSE_RN_DETAIL_QUANTITY = "Quantity";

	// 13.Warehouse_releasenote
	public static String ADD_WAREHOUSE_RELEASENOTE = "warehouseManager/releasenote/addReleasenotes";
	public static String ADD_WAREHOUSE_RELEASENOTE_BY_ORDER = "warehouseManager/releasenote/addReleasenotesByOrder";
	public static String UPDATE_WAREHOUSE_RELEASENOTE = "warehouseManager/releasenote/updateReleasenotes";
	public static String ADD_ALL_WAREHOUSE_RELEASENOTE = "warehouseManager/releasenote/addAllReleasenotes";
	public static String SHOW_REQUEST_WAREHOUSE_RELEASENOTE = "warehouseManager/orderRequest/showOrderRequest";
	public static String SHOW_ORDER_REQUEST_IN_WAREHOUSE = "warehouseManager/orderRequest/showOrderinWarehouse";
	public static String SHOW_ORDER_AND_REQUEST = "warehouseManager/OrderAndRequest/showOrderAndRequest";
	public static String SHOW_WAREHOUSE_RELEASENOTE = "warehouseManager/releasenote/WarehouseReleasenote";
	public static String SHOW_ORDER_IN_WAREHOUSE_RELEASENOTE = "warehouseManager/orderInfor/showOrderInWarehouse";
	public static String SHOW_ORDER_WAREHOUSE_RELEASENOTE = "warehouseManager/orderInfor/showOrderInfor";
	public static String SHOW_ORDER_IN_WAREHOUSE_DETAIL = "warehouseManager/orderInfor/showOrderInWarehouseDetail";
	public static String ADD_ALL_ORDER_RELEASENOTE = "warehouseManager/releasenote/addAllOrderReleasenotes";

	public static String TBL_WAREHOUSE_RELEASENOTE = "Warehouse_releasenote";
	public static String COL_WAREHOUSE_RELEASENOTE_EMPLOYEEID = "Employee_Id";
	public static String COL_WAREHOUSE_RELEASENOTE_WAREHOUSE_ID = "Warehouse_Id";
	public static String COL_WAREHOUSE_RELEASENOTE_WAREHOUSE_REQUEST_ID = "Request_Id";
	public static String COL_WAREHOUSE_RELEASENOTE_ID = "Id";
	public static String COL_WAREHOUSE_RELEASENOTE_NAME = "Name";
	public static String COL_WAREHOUSE_RELEASENOTE_DATE = "Date";
	public static String COL_WAREHOUSE_RELEASENOTE_STATUS = "Status";
	public static String COL_WAREHOUSE_RELEASENOTE_ORDER_ID = "Order_id";

	// 14.PAYMENT
	public static String TBL_PAYMENT = "Payment";
	public static String COL_PAYMENT_ID = "Id";
	public static String COL_PAYMENT_NAME = "Name";

	// 15.CUSTOMER
	public static String TBL_CUSTOMER = "Customer";
	public static String COL_CUSTOMER_ID = "Id";
	public static String COL_CUSTOMER_FIRST_NAME = "First_name";
	public static String COL_CUSTOMER_LAST_NAME = "Last_name";
	public static String COL_CUSTOMER_ADDRESS = "Address";
	public static String COL_CUSTOMER_PASSWORD = "Password";
	public static String COL_CUSTOMER_EMAIL = "Email";
	public static String COL_CUSTOMER_CREATION_TIME = "Creation_time";
	public static String COL_CUSTOMER_BIRTHOFDATE = "Dob";

	// 16.SHOPING CART
	public static String TBL_SHOPING_CART = "shopping_cart";
	public static String COL_SHOPING_CART_ID = "Id";
	public static String COL_SHOPING_CART_STATUS = "cart_status";
	public static String COL_SHOPING_CART_CUSTOMER_ID = "Customer_Id";
	public static String COL_SHOPING_CART_PRODUCT_ID = "Product_id";
	public static String COL_SHOPING_CART_QUANTITY = "Quantity";

	// 17.ORDER
	public static String TBL_ORDER = "Order";
	public static String COL_ORDER_ID = "Id";
	public static String COL_ORDER_CUSTOMER_ID = "Customer_Id";
	public static String COL_ORDER_STATUS = "Status";
	public static String COL_ORDER_EMPLOYEE = "Employee_Id";
	public static String COL_ORDER_PAYMENT_ID = "Payment_Id";
	public static String COL_ORDER_CUSNAME = "Cus_Name";
	public static String COL_ORDER_PHONE = "Phone";
	public static String COL_ORDER_ADDRESS = "Address";
	public static String COL_ORDER_PAYSTATUS = "Pay_status";
	public static String COL_ORDER_DATE = "Date";
	public static String COL_ORDER_COUPONID = "coupon_id";
	public static String COL_ORDER_DISCOUNT = "discount";
	public static String COL_ORDER_TOTALAMOUNT = "totalAmount";
	public static String COL_ORDER_SHIPPINGFEE = "shippingFee";
	public static String COL_ORDER_PAYMENTMETHOD = "paymentMethod";
	public static String COL_ORDER_NOTES = "notes";
	public static String COL_ORDER_ORDERID = "OrderID";
	public static String COL_ORDER_TRANSMOMOID = "Transaction_id";
	public static String COL_ORDER_PROVINCE_ID = "Province_Id";
	public static String COL_ORDER_DISTRICT_ID = "District_Id";
	public static String COL_ORDER_WARD_ID = "Ward_Id";
	public static String COL_ORDER_GHN_ORDER_CODE = "ghn_order_code";
	public static String COL_ORDER_TRACKING_CODE = "tracking_code";
	public static String COL_ORDER_EXPECTED_DELIVERY_TIME = "expected_delivery_time";
	public static String COL_ORDER_SHIPPING_STATUS = "shipping_status";
	public static String COL_ORDER_WAREHOUSE_ID = "Warehouse_Id";

	// 18.ORDER DETAIL
	public static String TBL_ORDER_DETAIL = "Order_detail";
	public static String COL_ORDER_DETAIL_ID = "Id";
	public static String COL_ORDER_DETAIL_STOCK_ID = "Stock_id";
	public static String COL_ORDER_DETAIL_ORDER_ID = "Order_Id";
	public static String COL_ORDER_DETAIL_PRICE = "Price";
	public static String COL_ORDER_DETAIL_STATUS = "Status";
	public static String COL_ORDER_DETAIL_PRODUCT_ID = "Product_Id";
	public static String COL_ORDER_DETAIL_QUANTITY = "Quantity";

	// 19.EMPLOYEE
	public static String EMPLOYEE_SHOWEMP = "admin/employee/showEmp";
	public static String EMPLOYEE_SHOWREGISTER = "admin/employee/showRegister";
	public static String EMPLOYEE_SHOWEMPLOYEEDETAIL = "admin/employee/showEmployeeDetail";
	public static String EMPLOYEE_SHOWUPDATEMPLOYEE = "admin/employee/showUpdateEmployee";
	public static String TBL_EMPLOYEE = "Employee";
	public static String COL_EMPLOYEE_ID = "Id";
	public static String COL_EMPLOYEE_FIRST_NAME = "First_name";
	public static String COL_EMPLOYEE_LAST_NAME = "Last_name";
	public static String COL_EMPLOYEE_ROLE_ID = "Role_Id";
	public static String COL_EMPLOYEE_PASSWORD = "Password";
	public static String COL_EMPLOYEE_PHONE = "Phone";
	// 20.ROLE
	public static String TBL_ROLE = "Role";
	public static String COL_ROLE_ID = "Id";
	public static String COL_ROLE_NAME = "Name";

	// 21.PRODUCT_CATEGORY
	public static String TBL_PRODUCT_SPE = "product_specifications";
	public static String COL_PRODUCT_SPE_ID = "Id";
	public static String COL_PRODUCT_SPE_NAME = "Name_spe";
	public static String COL_PRODUCT_SPE_DES = "Des_spe";
	public static String COL_PRODUCT_SPE_PRODUCTID= "Product_Id";
	//22.COUPON
	public static String DISCOUNT_LIST = "admin/discount/showDiscount";
	
	public static final String TBL_DISCOUNT = "tbl_discount";
    public static final String COL_DISCOUNT_ID = "id";
    public static final String COL_DISCOUNT_CODE = "code";
    public static final String COL_DISCOUNT_PERCENTAGE = "discount_percentage";
    public static final String COL_DISCOUNT_AMOUNT = "discount_amount";
    public static final String COL_DISCOUNT_EXPIRY_DATE = "expiry_date";
    public static final String COL_DISCOUNT_MIN_ORDER_VALUE = "min_order_value";
    public static final String COL_DISCOUNT_MAX_DISCOUNT_AMOUNT = "max_discount_amount";
    public static final String COL_DISCOUNT_STATUS = "status";
    public static final String COL_DISCOUNT_EMPLOYEE_ID = "Employee_Id";
    

	//23.FEEDBACK
	public static final String TBL_FEEDBACK = "Feedback";
	public static final String COL_FEEDBACK_ID = "Id";
	public static final String COL_FEEDBACK_PROID = "Product_Id";
	public static final String COL_FEEDBACK_ORDID = "OrderDetail_Id";
	public static final String COL_FEEDBACK_RATE = "Rating";
	public static final String COL_FEEDBACK_COMMENT = "Comment";
	public static final String COL_FEEDBACK_CREATEDATE = "Created_Date";
	public static final String COL_FEEDBACK_STATUS = "Status";
	// 24.REQUEST
	public static String ADD_ORDER_REQUEST = "businessManager/OrderRequest/addOrderRequest";
	public static String SHOW_ORDER_REQUEST = "businessManager/OrderRequest/orderRequest";
	public static String SHOW_ORDER_REQUEST_DETAIL_BUNISS = "businessManager/OrderRequest/showOrderRequestDetail";
	public static String ORDER_REQUEST_DETAIL = "businessManager/OrderRequest/orderRequestDetail";

	public static String REPLY_COMMENT = "businessManager/Comment/Replycomment";

	public static String TBL_REQUEST = "Request";
	public static String COL_REQUEST_ID = "Id";
	public static String COL_REQUEST_NAME = "Name";
	public static String COL_REQUEST_DATE = "Date";
	public static String COL_REQUEST_STATUS = "Status";
	public static String COL_REQUEST_WAREHOUSE = "Warehouse_Id";
	public static String COL_REQUEST_EMPLOYEE_ID = "Employee_Id";
	public static String COL_REQUEST_TYPE = "Type";
	public static String COL_REQUEST_ORDERID = "Order_Id";
	// 25.REQUEST DETAIL
	public static String SHOW_ORDER_REQUEST_DETAIL = "businessManager/OrderRequest/orderRequestDetail";
	public static String SHOW_REQUEST_IN_WAREHOUSE_DETAIL = "warehouseManager/orderRequest/showRequestInWarehouseDetail";

	public static String SHOW_INSUFFICIENT_OUTPUT_DETAIL = "warehouseManager/OrderAndRequest/showInsufficientOutputDetail";


	public static String TBL_REQUEST_DETAIL = "Request_detail";
	public static String COL_REQUEST_DETAIL_ID = "id";
	public static String COL_REQUEST_DETAIL_STATUS = "Status";
	public static String COL_REQUEST_DETAIL_ID_PRODUCT = "Id_product";
	public static String COL_REQUEST_DETAIL_QUANTITY_REQUESTED = "Quantity_requested";
	public static String COL_REQUEST_DETAIL_QUANTITY_EXPORTED = "Quantity_exported";
	public static String COL_REQUEST_DETAIL_REQUEST_ID = "Request_Id";

	public static String PRORESS_RETURN_ORDER = "businessManager/ReturnOrder/Showlistorder";
	public static String RETURN_BS_ORDER_DETAIL = "businessManager/ReturnOrder/showreorderde";
	public static String PRORESS_RETURN_ORDER_COMPLETE_ID = "businessManager/ReturnOrder/Showlistordercompleteid";
	public static String PRORESS_RETURN_ORDER_COMPLETE = "businessManager/ReturnOrder/Showlistordercomplete";

	//25.RETURN ORDER
	
	public static String RETURN_ORDERS_WAM = "warehouseManager/returnorders/showreorders";
	public static String RETURN_ORDERS_WAMCOM = "warehouseManager/returnorders/showreorderscomplete";
	
		public static String TBL_RETURN_ORDER = "Return_Order";
		public static String COL_RETURN_ORDER_ID = "Id";
		public static String COL_RETURN_ORDER_ORDER_ID = "Order_Id";
		public static String COL_RETURN_ORDER_DATE = "Return_Date";
		public static String COL_RETURN_ORDER_STATUS = "Status";
		public static String COL_RETURN_ORDER_NOTE = "Note";
		public static String COL_RETURN_ORDER_TOTAL_AMOUNT = "Total_Amount";
		public static String COL_RETURN_ORDER_DISCOUNT_AMOUNT = "Discount_Amount";
		public static String COL_RETURN_ORDER_FINAL_AMOUNT = "Final_Amount";
		public static String COL_RETURN_ORDER_MESSAGE = "Message";
		public static String COL_RETURN_ORDER_EMPLOYEE_ID = "Employee_Id";
	
	
		//25.RETURN ORDER DETAIL
		public static String TBL_RETURN_ORDER_DETAIL = "Return_Order_Detail";
		public static String COL_RETURN_DETAIL_ID = "Id";
		public static String COL_RETURN_DETAIL_RETURN_ID = "Return_Order_Id";
		public static String COL_RETURN_DETAIL_ORDER_DETAIL_ID = "Order_Detail_Id";
		public static String COL_RETURN_DETAIL_QUANTITY = "Quantity";
		public static String COL_RETURN_DETAIL_REASON = "Reason";
		public static String COL_RETURN_DETAIL_AMOUNT = "Amount";
	// 26.CUSTOMER

	// 21.CUSTOMER

	public static String CUS_SHOWPAGEMAIN = "customer/pagemain";
	public static String CUS_SHOPPINGPAGE = "customer/shoppingpage";
	public static String CUS_DETAILPROPAGE = "customer/detailproduct";
	public static String CUS_CARTPAGE = "customer/cart";
	public static String CUS_SIGNINPAGE = "customer/signin";
	public static String CUS_SIGNUPPAGE = "customer/signup";
	public static String CUS_COFIRMPAGE = "customer/corfirmregister";
	public static String CUS_CUSINFOPAGE = "customer/cusinfo";
	public static String CUS_CUSCHANGEPASSWORDPAGE = "customer/changepassword";
	public static String CUS_CUSCHECKOUTPAGE = "customer/checkout";
	public static String CUS_ORDEREDPAGE = "customer/ordereds";
	public static String CUS_ORDEREDDETAILPAGE = "customer/orderdetail";

	public static String CUS_CONTACTPAGE = "customer/contact";


	//21.Product_Img
	public static String PRODUCT_IMAGES_SHOWADDPI ="admin/product/showAddPImg";

	public static String TBL_PRODUCT_IMG = "product_img";
	public static String COL_PRODUCT_IMG_ID = "Id";
	public static String COL_PRODUCT_IMG_URL = "Img_url";
	public static String COL_PRODUCT_IMG_PRODUCT_ID = "Product_id";
	// 22.product_spe
	public static String PRODUCT_SPE_SHOWADDPS = "admin/product/showAddPs";
	public static String PRODUCT_SPE_SHOWUPDATEPS = "admin/product/showUpdatePs";

	public static String COL_PRODUCT_SPE_NAME_SPE = "Name_spe";
	public static String COL_PRODUCT_SPE_DES_SPE = "Des_spe";
	public static String COL_PRODUCT_SPE_PRODUCT_ID = "Product_id";
	// 23.Employee_warehouse
	public static String EMPLOYEE_WAREHOUSE_SHOWEMPWARE = "admin/warehouse/showEmpWare";
	public static String EMPLOYEE_WAREHOUSE_SHOWWAREHOUSEMANAGEMENTLEVEL = "admin/warehouse/showWarehouseManagementLevel";
	public static String EMPLOYEE_WAREHOUSE_SHOWUPDATEWAREHOUSEMANAGEMENTLEVEL = "admin/warehouse/showUpdateWarehouseManagementLevel";
	public static String TBL_EMPLOYEE_WAREHOUSE = "employee_warehouse";
	public static String COL_EMPLOYEE_WAREHOUSE_ID = "Id";
	public static String COL_EMPLOYEE_WAREHOUS_EMPLOYEE_ID = "Employee_Id";
	public static String COL_EMPLOYEE_WAREHOUS_WAREHOUSE_ID = "Warehouse_Id";

	// Bảng Comments
	public static final String TBL_COMMENTS = "comments";
	public static final String COL_COMMENT_ID = "id";
	public static final String COL_COMMENT_CONTENT = "content";
	public static final String COL_COMMENT_PRODUCT_ID = "product_id";
	public static final String COL_COMMENT_CUSTOMER_ID = "customer_id";
	public static final String COL_COMMENT_STAFF_ID = "employee_id";
	public static final String COL_COMMENT_PARENT_ID = "parent_id";
	public static final String COL_COMMENT_STATUS = "status";
	public static final String COL_COMMENT_CREATED_AT = "created_at";
	public static final String COL_COMMENT_UPDATED_AT = "updated_at";

	// Bảng Banned Keywords
	public static final String TBL_BANNED_KEYWORDS = "banned_keywords";
	public static final String COL_BANNED_KEYWORD_ID = "id";
	public static final String COL_BANNED_KEYWORD = "keyword";
	public static final String COL_BANNED_KEYWORD_IS_ACTIVE = "is_active";
	public static final String COL_BANNED_KEYWORD_CREATED_AT = "created_at";
	public static final String COL_BANNED_KEYWORD_UPDATED_AT = "updated_at";
	// Expense Types
	public static String TBL_EXPENSE_TYPES = "Expense_Types";
	public static String COL_EXPENSE_TYPE_ID = "Id";
	public static String COL_EXPENSE_TYPE_NAME = "Name";
	public static String COL_EXPENSE_TYPE_DESC = "Description";
	public static String COL_EXPENSE_TYPE_IS_FIXED = "Is_Fixed";
	public static String COL_EXPENSE_TYPE_IS_ACTIVE = "Is_Active";
	public static String COL_EXPENSE_TYPE_CREATED_AT = "Created_At";
	public static String COL_EXPENSE_TYPE_UPDATED_AT = "Updated_At";

	// Expense History

	public static String EXPENSE_SHOWEXPENSEPAGE = "admin/Expense/showHisExpense";
	public static String TBL_EXPENSE_HISTORY = "Expense_History";
	public static String COL_EXPENSE_HISTORY_ID = "Id";
	public static String COL_EXPENSE_HISTORY_TYPE_ID = "Expense_Type_Id";
	public static String COL_EXPENSE_HISTORY_AMOUNT = "Amount";
	public static String COL_EXPENSE_HISTORY_START_DATE = "Start_Date";
	public static String COL_EXPENSE_HISTORY_END_DATE = "End_Date";
	public static String COL_EXPENSE_HISTORY_NOTE = "Note";
	public static String COL_EXPENSE_HISTORY_CREATED_BY = "Created_By";
	public static String COL_EXPENSE_HISTORY_CREATED_AT = "Created_At";
	public static String COL_EXPENSE_HISTORY_UPDATED_AT = "Updated_At";

	// Salary Types
	public static final String SALARY_TYPES = "admin/Salary/salaryTypes";
	public static String TBL_SALARY_TYPES = "Salary_Types";
	public static String COL_SALARY_TYPE_ID = "Id";
	public static String COL_SALARY_TYPE_NAME = "Name";
	public static String COL_SALARY_TYPE_DESC = "Description";
	public static String COL_SALARY_TYPE_IS_ACTIVE = "Is_Active";

	// Employee Salary History
	public static String TBL_EMPLOYEE_SALARY_HISTORY = "Employee_Salary_History";	
	public static String COL_SALARY_HISTORY_ID = "Id";
	public static String COL_SALARY_HISTORY_EMPLOYEE_ID = "Employee_Id";
	public static String COL_SALARY_HISTORY_TYPE_ID = "Salary_Type_Id";
	public static String COL_SALARY_HISTORY_AMOUNT = "Amount";
	public static String COL_SALARY_HISTORY_START_DATE = "Start_Date";
	public static String COL_SALARY_HISTORY_END_DATE = "End_Date";
	public static String COL_SALARY_HISTORY_NOTE = "Note";
	public static String COL_SALARY_HISTORY_CREATED_BY = "Created_By";
	public static String COL_SALARY_HISTORY_CREATED_AT = "Created_At";

	// Tax History
	public static String TAX_SHOWTAXPAGE = "admin/tax/showTax";
	public static String TBL_TAX_HISTORY = "Tax_History";
	public static String COL_TAX_HISTORY_ID = "Id";
	public static String COL_TAX_HISTORY_TYPE = "Tax_Type";
	public static String COL_TAX_HISTORY_PERIOD_START = "Period_Start";
	public static String COL_TAX_HISTORY_PERIOD_END = "Period_End";
	public static String COL_TAX_HISTORY_REVENUE = "Amount";
	public static String COL_TAX_HISTORY_RATE = "Tax_Rate";
	public static String COL_TAX_HISTORY_AMOUNT = "Tax_Amount";
	public static String COL_TAX_HISTORY_STATUS = "Payment_Status";
	public static String COL_TAX_HISTORY_PAYMENT_DATE = "Payment_Date";
	public static String COL_TAX_HISTORY_NOTE = "Note";
	public static String COL_TAX_HISTORY_CREATED_AT = "Created_At";
	public static String COL_TAX_HISTORY_CREATED_BY = "Created_By";

	public static String ADMIN_STATISTICSPAGE = "admin/statistics/statistics";
	
	// Employee Warehouse
	public static String EMPLOYEE_WAREHOUSE_MANAGER = "warehouseManager/employee/showEmployee";
	public static String EMPLOYEE_BUSINESS_MANAGER = "businessManager/employee/showEmployee";
	
	// Customer buniss
	public static String SHOW_CUSTOMER_BUSINESS = "businessManager/customer/showCustomer";
	public static String SHOW_CUSTOMER_ORDER_BUSINESS = "businessManager/customer/showCustomerOrder";
	
	// order waiting 
	public static String SHOW_ORDER_STATUS_WAITING = "warehouseManager/orderInfor/orderWaiting";



	public static String PAGE_LISTCHAT_NOEM = "businessManager/chatsp/listchatnoem";
	public static String PAGE_CHAT_EMPLOYEE= "businessManager/chatsp/chatsp";
	public static String PAGE_LISTCHAT_EMID = "businessManager/chatsp/listchatbyemid";
	public static String TBL_CHAT_ROOM = "Chat_Room";
	public static String COL_CHAT_ROOM_ID = "Id";
	public static String COL_CHAT_ROOM_ORDER_ID = "Order_Id";
	public static String COL_CHAT_ROOM_EMPLOYEE_ID = "Employee_Id";
	public static String COL_CHAT_ROOM_STATUS = "Status";
	public static String COL_CHAT_ROOM_LAST_ACTIVITY = "Last_Activity";

	// ChatMessage table
	public static String TBL_CHAT_MESSAGE = "Chat_Message";
	public static String COL_CHAT_MESSAGE_ID = "Id";
	public static String COL_CHAT_MESSAGE_CHATROOM_ID = "ChatRoom_Id";
	public static String COL_CHAT_MESSAGE_EMPLOYEE_ID = "Employee_Id";
	public static String COL_CHAT_MESSAGE_CONTENT = "Message";
	public static String COL_CHAT_MESSAGE_TIMESTAMP = "Timestamp";
	public static String COL_CHAT_MESSAGE_SENDER_TYPE = "Sender_Type";
	public static String COL_CHAT_MESSAGE_IS_READ = "Is_Read";
	
}
