
-- -----------------------------------------------------------------------------
-- ACL MODULES (Hierarchy)
-- Defines the navigation tree for permissions.
-- Root modules have parent_id = 0.
-- -----------------------------------------------------------------------------

INSERT INTO `sys_acl` (`code`, `name`, `acl_module_id`, `url`, `type`, `status`, `seq`, `remark`, `operator`, `operate_time`, `operate_ip`) VALUES
-- 1. Navigation & View (Type 1: Menu)
('201603100001', 'Product Management Page', 1, '/sys/product/product.page', 1, 1, 1, 'Main product inventory view', 'Admin', '2016-03-10 09:00:00', '127.0.0.1'),
('201603100002', 'Product Category Page', 1, '/sys/product/category.page', 1, 1, 2, 'Category tree management', 'Admin', '2016-03-10 09:00:00', '127.0.0.1'),
('201603100003', 'Inventory Dashboard', 1, '/sys/product/inventory.page', 1, 1, 3, 'Stock level overview', 'Admin', '2016-03-10 09:00:00', '127.0.0.1'),
('201603100004', 'Price Management Page', 1, '/sys/product/price.page', 1, 1, 4, 'Bulk pricing adjustment entry', 'Admin', '2016-03-10 09:00:00', '127.0.0.1'),

-- 2. Core Product Actions (Type 2: Button)
('201603100005', 'Query Product List', 1, '/sys/product/page.json', 2, 1, 5, 'Search and filter products', 'Admin', '2016-03-10 09:05:00', '127.0.0.1'),
('201603100006', 'Save New Product', 1, '/sys/product/save.json', 2, 1, 6, 'Create new product record', 'Admin', '2016-03-10 09:05:00', '127.0.0.1'),
('201603100007', 'Update Product Info', 1, '/sys/product/update.json', 2, 1, 7, 'Modify existing product details', 'Admin', '2016-03-10 09:10:00', '127.0.0.1'),
('201603100008', 'Delete Product', 1, '/sys/product/delete.json', 2, 1, 8, 'Remove product from system', 'Admin', '2016-03-10 09:10:00', '127.0.0.1'),
('201603100009', 'List Product (Online)', 1, '/sys/product/online.json', 2, 1, 9, 'Mark product as active/saleable', 'Admin', '2016-03-10 09:15:00', '127.0.0.1'),
('201603100010', 'Delist Product (Offline)', 1, '/sys/product/offline.json', 2, 1, 10, 'Mark product as inactive', 'Admin', '2016-03-10 09:15:00', '127.0.0.1'),
('201603100011', 'View Product Detail', 1, '/sys/product/detail.json', 2, 1, 11, 'Access full product specs', 'Admin', '2016-03-10 09:20:00', '127.0.0.1'),
('201603100012', 'Batch Update Status', 1, '/sys/product/batchUpdate.json', 2, 1, 12, 'Bulk online/offline switch', 'Admin', '2016-03-10 09:20:00', '127.0.0.1'),

-- 3. Inventory & Warehouse Operations
('201603100013', 'Update Stock Quantity', 1, '/sys/product/updateStock.json', 2, 1, 13, 'Manual inventory adjustment', 'Admin', '2016-03-10 10:00:00', '127.0.0.1'),
('201603100014', 'View Stock History', 1, '/sys/product/stockLog.json', 2, 1, 14, 'Audit trail for stock changes', 'Admin', '2016-03-10 10:00:00', '127.0.0.1'),
('201603100015', 'Assign Warehouse', 1, '/sys/product/assignWarehouse.json', 2, 1, 15, 'Bind product to specific location', 'Admin', '2016-03-10 10:05:00', '127.0.0.1'),
('201603100016', 'Set Low Stock Alert', 1, '/sys/product/setAlert.json', 2, 1, 16, 'Threshold for notifications', 'Admin', '2016-03-10 10:05:00', '127.0.0.1'),

-- 4. Pricing & Promotions
('201603100017', 'Modify Sale Price', 1, '/sys/product/updatePrice.json', 2, 1, 17, 'Change consumer-facing price', 'Admin', '2016-03-10 11:00:00', '127.0.0.1'),
('201603100018', 'View Cost Price', 1, '/sys/product/costDetail.json', 2, 1, 18, 'Sensitive financial data access', 'Admin', '2016-03-10 11:00:00', '127.0.0.1'),
('201603100019', 'Apply Promotion Tag', 1, '/sys/product/applyPromo.json', 2, 1, 19, 'Attach marketing labels', 'Admin', '2016-03-10 11:05:00', '127.0.0.1'),
('201603100020', 'Set Bulk Discount', 1, '/sys/product/bulkDiscount.json', 2, 1, 20, 'Volume-based pricing rules', 'Admin', '2016-03-10 11:05:00', '127.0.0.1'),

-- 5. Media & Content Governance
('201603100021', 'Upload Product Image', 1, '/sys/product/uploadImg.json', 2, 1, 21, 'Primary and gallery images', 'Admin', '2016-03-10 13:00:00', '127.0.0.1'),
('201603100022', 'Delete Media File', 1, '/sys/product/deleteMedia.json', 2, 1, 22, 'Remove image or video', 'Admin', '2016-03-10 13:00:00', '127.0.0.1'),
('201603100023', 'Update SEO Metadata', 1, '/sys/product/updateSeo.json', 2, 1, 23, 'Tags and descriptions for search', 'Admin', '2016-03-10 13:10:00', '127.0.0.1'),
('201603100024', 'Manage Product Specs', 1, '/sys/product/saveSpec.json', 2, 1, 24, 'Attributes like size/color', 'Admin', '2016-03-10 13:10:00', '127.0.0.1'),

-- 6. Category Management
('201603100025', 'Save Category', 1, '/sys/product/saveCategory.json', 2, 1, 25, 'Create new category node', 'Admin', '2016-03-10 14:00:00', '127.0.0.1'),
('201603100026', 'Delete Category', 1, '/sys/product/delCategory.json', 2, 1, 26, 'Remove category and children', 'Admin', '2016-03-10 14:00:00', '127.0.0.1'),
('201603100027', 'Sort Categories', 1, '/sys/product/sortCategory.json', 2, 1, 27, 'Adjust display order', 'Admin', '2016-03-10 14:05:00', '127.0.0.1'),

-- 7. Analytics & Auditing (Type 3: API/Other)
('201603100028', 'View Change History', 1, '/sys/product/log.json', 3, 1, 28, 'Audit trail for all edits', 'Admin', '2016-03-10 15:00:00', '127.0.0.1'),
('201603100029', 'Check Price Sync', 1, '/api/product/priceSync', 3, 1, 29, 'Verify external channel pricing', 'Admin', '2016-03-10 15:00:00', '127.0.0.1'),
('201603100030', 'Inventory Health Check', 1, '/api/product/health', 3, 1, 30, 'Internal consistency API', 'Admin', '2016-03-10 15:10:00', '127.0.0.1'),

-- 8. Data Import/Export
('201603100031', 'Export Catalog (Excel)', 1, '/sys/product/export.json', 2, 1, 31, 'Download full inventory', 'Admin', '2016-03-10 16:00:00', '127.0.0.1'),
('201603100032', 'Bulk Import Products', 1, '/sys/product/import.json', 2, 1, 32, 'Upload CSV/Excel data', 'Admin', '2016-03-10 16:00:00', '127.0.0.1'),
('201603100033', 'Generate SKU Barcode', 1, '/sys/product/barcode.json', 2, 1, 33, 'Printable barcode generation', 'Admin', '2016-03-10 16:10:00', '127.0.0.1'),

-- 9. Advanced Governance & Approval
('201603100034', 'Submit for Listing Review', 1, '/sys/product/submitReview.json', 2, 1, 34, 'Start approval workflow', 'Admin', '2016-03-10 17:00:00', '127.0.0.1'),
('201603100035', 'Approve Listing', 1, '/sys/product/approve.json', 2, 1, 35, 'Final clearance for sale', 'Admin', '2016-03-10 17:00:00', '127.0.0.1'),
('201603100036', 'Reject Listing', 1, '/sys/product/reject.json', 2, 1, 36, 'Deny listing with feedback', 'Admin', '2016-03-10 17:05:00', '127.0.0.1'),
('201603100037', 'Archive Obsolete Product', 1, '/sys/product/archive.json', 2, 1, 37, 'Hide from main lists', 'Admin', '2016-03-10 17:05:00', '127.0.0.1'),

-- 10. Specialized & Integration Actions
('201603100038', 'Sync to Marketplace', 1, '/sys/product/syncMarket.json', 2, 1, 38, 'Push data to external platforms', 'Admin', '2016-03-10 17:10:00', '127.0.0.1'),
('201603100039', 'Update Tax Category', 1, '/sys/product/updateTax.json', 2, 1, 39, 'Financial compliance change', 'Admin', '2016-03-10 17:15:00', '127.0.0.1'),
('201603100040', 'View Competitor Pricing', 1, '/sys/product/compare.json', 3, 1, 40, 'Internal market analysis', 'Admin', '2016-03-10 17:15:00', '127.0.0.1'),
('201603100041', 'Lock Product for Edit', 1, '/sys/product/lock.json', 3, 1, 41, 'Concurrency control', 'Admin', '2016-03-10 17:20:00', '127.0.0.1'),
('201603100042', 'Unlock Product', 1, '/sys/product/unlock.json', 3, 1, 42, 'Manual lock override', 'Admin', '2016-03-10 17:20:00', '127.0.0.1'),
('201603100043', 'Apply Seasonal Pricing', 1, '/sys/product/seasonal.json', 2, 1, 43, 'Automated price schedule', 'Admin', '2016-03-10 17:25:00', '127.0.0.1'),
('201603100044', 'Manage Product Variants', 1, '/sys/product/variants.json', 2, 1, 44, 'Sub-SKU management', 'Admin', '2016-03-10 17:25:00', '127.0.0.1'),
('201603100045', 'Internal API: Notify Marketing', 1, '/api/product/marketingNotify', 3, 1, 45, 'Trigger campaign automation', 'Admin', '2016-03-10 17:30:00', '127.0.0.1');





INSERT INTO `sys_acl` (`code`, `name`, `acl_module_id`, `url`, `type`, `status`, `seq`, `remark`, `operator`, `operate_time`, `operate_ip`) VALUES
                                                                                                                                                ('201605010001', 'Order List Page', 2, '/sys/order/order.page', 1, 1, 1, 'Main order tracking view', 'Admin', '2016-05-01 10:00:00', '192.168.1.10'),
                                                                                                                                                ('201605010002', 'Return Requests Page', 2, '/sys/order/return.page', 1, 1, 2, 'RMA management entry', 'Admin', '2016-05-01 10:00:00', '192.168.1.10'),
                                                                                                                                                ('201605010003', 'Order Stats Dashboard', 2, '/sys/order/stats.page', 1, 1, 3, 'Overview of sales performance', 'Admin', '2016-05-01 10:00:00', '192.168.1.10'),
                                                                                                                                                ('201605010004', 'Abnormal Order Page', 2, '/sys/order/abnormal.page', 1, 1, 4, 'Orders requiring manual review', 'Admin', '2016-05-01 10:00:00', '192.168.1.10'),
                                                                                                                                                ('201605010005', 'Search Orders', 2, '/sys/order/page.json', 2, 1, 5, 'Search and filter capability', 'Admin', '2016-05-01 10:05:00', '192.168.1.10'),
                                                                                                                                                ('201605010006', 'View Order Detail', 2, '/sys/order/detail.json', 2, 1, 6, 'Access specific order data', 'Admin', '2016-05-01 10:05:00', '192.168.1.10'),
                                                                                                                                                ('201605010007', 'Confirm Order', 2, '/sys/order/confirm.json', 2, 1, 7, 'Validate new order', 'Admin', '2016-05-01 10:10:00', '192.168.1.10'),
                                                                                                                                                ('201605010008', 'Cancel Order', 2, '/sys/order/cancel.json', 2, 1, 8, 'Nullify order before shipping', 'Admin', '2016-05-01 10:10:00', '192.168.1.10'),
                                                                                                                                                ('201605010009', 'Mark as Shipped', 2, '/sys/order/ship.json', 2, 1, 9, 'Update status to shipped', 'Admin', '2016-05-01 10:15:00', '192.168.1.10'),
                                                                                                                                                ('201605010010', 'Edit Shipping Address', 2, '/sys/order/updateAddress.json', 2, 1, 10, 'Modify delivery location', 'Admin', '2016-05-01 10:15:00', '192.168.1.10'),
                                                                                                                                                ('201605010011', 'Update Tracking No', 2, '/sys/order/updateLogistics.json', 2, 1, 11, 'Set carrier tracking ID', 'Admin', '2016-05-01 10:20:00', '192.168.1.10'),
                                                                                                                                                ('201605010012', 'Print Invoice', 2, '/sys/order/print.json', 2, 1, 12, 'Generate printable PDF', 'Admin', '2016-05-01 10:20:00', '192.168.1.10'),
                                                                                                                                                ('201605010013', 'Apply for Refund', 2, '/sys/order/applyRefund.json', 2, 1, 13, 'Initiate money back process', 'Admin', '2016-05-01 11:00:00', '192.168.1.10'),
                                                                                                                                                ('201605010014', 'Approve Refund', 2, '/sys/order/approveRefund.json', 2, 1, 14, 'Managerial financial approval', 'Admin', '2016-05-01 11:00:00', '192.168.1.10'),
                                                                                                                                                ('201605010015', 'Reject Refund', 2, '/sys/order/rejectRefund.json', 2, 1, 15, 'Deny refund request', 'Admin', '2016-05-01 11:05:00', '192.168.1.10'),
                                                                                                                                                ('201605010016', 'Manual Price Change', 2, '/sys/order/adjustPrice.json', 2, 1, 16, 'Override total order amount', 'Admin', '2016-05-01 11:05:00', '192.168.1.10'),
                                                                                                                                                ('201605010017', 'Verify Payment', 2, '/sys/order/checkPayment.json', 2, 1, 17, 'Sync status with bank/gateway', 'Admin', '2016-05-01 11:10:00', '192.168.1.10'),
                                                                                                                                                ('201605010018', 'Assign Warehouse', 2, '/sys/order/assignWarehouse.json', 2, 1, 18, 'Select fulfillment center', 'Admin', '2016-05-01 11:30:00', '192.168.1.10'),
                                                                                                                                                ('201605010019', 'Confirm Goods Arrival', 2, '/sys/order/confirmArrival.json', 2, 1, 19, 'For returned items', 'Admin', '2016-05-01 11:30:00', '192.168.1.10'),
                                                                                                                                                ('201605010020', 'Generate Picking List', 2, '/sys/order/pickingList.json', 2, 1, 20, 'Internal warehouse document', 'Admin', '2016-05-01 11:35:00', '192.168.1.10'),
                                                                                                                                                ('201605010021', 'Split Order', 2, '/sys/order/split.json', 2, 1, 21, 'Ship from multiple locations', 'Admin', '2016-05-01 11:35:00', '192.168.1.10'),
                                                                                                                                                ('201605010022', 'Add Order Remark', 2, '/sys/order/remark.json', 2, 1, 22, 'CS internal notes', 'Admin', '2016-05-01 12:00:00', '192.168.1.10'),
                                                                                                                                                ('201605010023', 'Send SMS Update', 2, '/sys/order/sendSms.json', 2, 1, 23, 'Notify customer via phone', 'Admin', '2016-05-01 12:00:00', '192.168.1.10'),
                                                                                                                                                ('201605010024', 'Send Email Confirmation', 2, '/sys/order/sendEmail.json', 2, 1, 24, 'Notify customer via email', 'Admin', '2016-05-01 12:05:00', '192.168.1.10'),
                                                                                                                                                ('201605010025', 'Urge Fulfillment', 2, '/sys/order/urge.json', 2, 1, 25, 'Internal alert for slow orders', 'Admin', '2016-05-01 12:05:00', '192.168.1.10'),
                                                                                                                                                ('201605010026', 'Export Orders (Excel)', 2, '/sys/order/export.json', 2, 1, 26, 'Bulk download records', 'Admin', '2016-05-01 13:00:00', '192.168.1.10'),
                                                                                                                                                ('201605010027', 'Batch Confirm', 2, '/sys/order/batchConfirm.json', 2, 1, 27, 'High volume processing', 'Admin', '2016-05-01 13:00:00', '192.168.1.10'),
                                                                                                                                                ('201605010028', 'Import Marketplace Orders', 2, '/sys/order/import.json', 2, 1, 28, 'Third-party sync', 'Admin', '2016-05-01 13:10:00', '192.168.1.10'),
                                                                                                                                                ('201605010029', 'Archive Orders', 2, '/sys/order/archive.json', 2, 1, 29, 'Move to long-term storage', 'Admin', '2016-05-01 13:20:00', '192.168.1.10'),
                                                                                                                                                ('201605010030', 'View Change Log', 2, '/sys/order/log.json', 3, 1, 30, 'Audit history for single order', 'Admin', '2016-05-01 14:00:00', '192.168.1.10'),
                                                                                                                                                ('201605010031', 'Lock Order', 2, '/sys/order/lock.json', 3, 1, 31, 'Prevent concurrent editing', 'Admin', '2016-05-01 14:05:00', '192.168.1.10'),
                                                                                                                                                ('201605010032', 'Unlock Order', 2, '/sys/order/unlock.json', 3, 1, 32, 'Release concurrency lock', 'Admin', '2016-05-01 14:05:00', '192.168.1.10'),
                                                                                                                                                ('201605010033', 'Internal API: Validate Stock', 2, '/api/order/checkStock', 3, 1, 33, 'Service-to-service call', 'Admin', '2016-05-01 14:10:00', '192.168.1.10'),
                                                                                                                                                ('201605010034', 'Internal API: Calc Tax', 2, '/api/order/calcTax', 3, 1, 34, 'Real-time tax calculation', 'Admin', '2016-05-01 14:10:00', '192.168.1.10'),
                                                                                                                                                ('201605010035', 'Merge Orders', 2, '/sys/order/merge.json', 2, 1, 35, 'Combine multiple orders for one customer', 'Admin', '2016-05-01 15:00:00', '192.168.1.10'),
                                                                                                                                                ('201605010036', 'Apply Coupon Post-Order', 2, '/sys/order/applyCoupon.json', 2, 1, 36, 'Customer loyalty correction', 'Admin', '2016-05-01 15:00:00', '192.168.1.10'),
                                                                                                                                                ('201605010037', 'Re-issue Order', 2, '/sys/order/reissue.json', 2, 1, 37, 'Send replacement for damaged goods', 'Admin', '2016-05-01 15:10:00', '192.168.1.10'),
                                                                                                                                                ('201605010038', 'Hold Order', 2, '/sys/order/hold.json', 2, 1, 38, 'Pause processing for security check', 'Admin', '2016-05-01 15:15:00', '192.168.1.10'),
                                                                                                                                                ('201605010039', 'Release Order Hold', 2, '/sys/order/release.json', 2, 1, 39, 'Continue processing after check', 'Admin', '2016-05-01 15:15:00', '192.168.1.10'),
                                                                                                                                                ('201605010040', 'Access VIP Order Queue', 2, '/sys/order/vip.page', 1, 1, 40, 'Prioritized handling menu', 'Admin', '2016-05-01 15:30:00', '192.168.1.10'),
                                                                                                                                                ('201605010041', 'Flag Fraudulent Order', 2, '/sys/order/markFraud.json', 2, 1, 41, 'Security/Risk management', 'Admin', '2016-05-01 15:35:00', '192.168.1.10'),
                                                                                                                                                ('201605010042', 'Blacklist Customer', 2, '/sys/order/blacklist.json', 2, 1, 42, 'Prevent future orders from user', 'Admin', '2016-05-01 15:40:00', '192.168.1.10'),
                                                                                                                                                ('201605010043', 'Force Close Order', 2, '/sys/order/forceClose.json', 2, 1, 43, 'Administrative override', 'Admin', '2016-05-01 15:45:00', '192.168.1.10');


INSERT INTO `sys_acl` (`code`, `name`, `acl_module_id`, `url`, `type`, `status`, `seq`, `remark`, `operator`, `operate_time`, `operate_ip`) VALUES
                                                                                                                                                ('201608150001', 'Announcement List Page', 3, '/sys/notice/notice.page', 1, 1, 1, 'Main dashboard for all notices', 'Admin', '2016-08-15 09:00:00', '127.0.0.1'),
                                                                                                                                                ('201608150002', 'Create Announcement Page', 3, '/sys/notice/save.page', 1, 1, 2, 'Entry form for new content', 'Admin', '2016-08-15 09:00:00', '127.0.0.1'),
                                                                                                                                                ('201608150003', 'Draft Management Page', 3, '/sys/notice/draft.page', 1, 1, 3, 'View unpublished work', 'Admin', '2016-08-15 09:00:00', '127.0.0.1'),
                                                                                                                                                ('201608150004', 'Notice Category Page', 3, '/sys/notice/category.page', 1, 1, 4, 'Manage announcement types', 'Admin', '2016-08-15 09:00:00', '127.0.0.1'),
                                                                                                                                                ('201608150005', 'Query Notice List', 3, '/sys/notice/page.json', 2, 1, 5, 'Search and filter notices', 'Admin', '2016-08-15 09:05:00', '127.0.0.1'),
                                                                                                                                                ('201608150006', 'Save New Notice', 3, '/sys/notice/save.json', 2, 1, 6, 'Persist new announcement', 'Admin', '2016-08-15 09:05:00', '127.0.0.1'),
                                                                                                                                                ('201608150007', 'Update Notice Content', 3, '/sys/notice/update.json', 2, 1, 7, 'Edit existing records', 'Admin', '2016-08-15 09:10:00', '127.0.0.1'),
                                                                                                                                                ('201608150008', 'Delete Announcement', 3, '/sys/notice/delete.json', 2, 1, 8, 'Remove notice permanently', 'Admin', '2016-08-15 09:10:00', '127.0.0.1'),
                                                                                                                                                ('201608150009', 'Publish Announcement', 3, '/sys/notice/publish.json', 2, 1, 9, 'Make notice visible to users', 'Admin', '2016-08-15 09:15:00', '127.0.0.1'),
                                                                                                                                                ('201608150010', 'Recall Announcement', 3, '/sys/notice/recall.json', 2, 1, 10, 'Take down live notice', 'Admin', '2016-08-15 09:15:00', '127.0.0.1'),
                                                                                                                                                ('201608150011', 'Set as Top/Sticky', 3, '/sys/notice/setTop.json', 2, 1, 11, 'Pin notice to the top', 'Admin', '2016-08-15 09:20:00', '127.0.0.1'),
                                                                                                                                                ('201608150012', 'Cancel Top/Sticky', 3, '/sys/notice/cancelTop.json', 2, 1, 12, 'Unpin notice', 'Admin', '2016-08-15 09:20:00', '127.0.0.1'),
                                                                                                                                                ('201608150013', 'Upload Image', 3, '/sys/notice/uploadImg.json', 2, 1, 13, 'Attach images to notice', 'Admin', '2016-08-15 10:00:00', '127.0.0.1'),
                                                                                                                                                ('201608150014', 'Upload Attachment', 3, '/sys/notice/uploadFile.json', 2, 1, 14, 'Attach PDFs or docs', 'Admin', '2016-08-15 10:00:00', '127.0.0.1'),
                                                                                                                                                ('201608150015', 'Preview Content', 3, '/sys/notice/preview.json', 2, 1, 15, 'View how it looks before live', 'Admin', '2016-08-15 10:05:00', '127.0.0.1'),
                                                                                                                                                ('201608150016', 'Submit for Review', 3, '/sys/notice/submit.json', 2, 1, 16, 'Send to manager for approval', 'Admin', '2016-08-15 11:00:00', '127.0.0.1'),
                                                                                                                                                ('201608150017', 'Approve Notice', 3, '/sys/notice/approve.json', 2, 1, 17, 'Final audit clearance', 'Admin', '2016-08-15 11:00:00', '127.0.0.1'),
                                                                                                                                                ('201608150018', 'Reject Notice', 3, '/sys/notice/reject.json', 2, 1, 18, 'Send back for corrections', 'Admin', '2016-08-15 11:05:00', '127.0.0.1'),
                                                                                                                                                ('201608150019', 'Set Audience Group', 3, '/sys/notice/setAudience.json', 2, 1, 19, 'Select roles/depts to see notice', 'Admin', '2016-08-15 13:00:00', '127.0.0.1'),
                                                                                                                                                ('201608150020', 'Schedule Publish Time', 3, '/sys/notice/schedule.json', 2, 1, 20, 'Set future publish date', 'Admin', '2016-08-15 13:00:00', '127.0.0.1'),
                                                                                                                                                ('201608150021', 'Set Expiration Date', 3, '/sys/notice/expire.json', 2, 1, 21, 'Set auto-offline date', 'Admin', '2016-08-15 13:10:00', '127.0.0.1'),
                                                                                                                                                ('201608150022', 'View Read Statistics', 3, '/sys/notice/readStats.json', 3, 1, 22, 'Check who has read the notice', 'Admin', '2016-08-15 14:00:00', '127.0.0.1'),
                                                                                                                                                ('201608150023', 'View Operation Log', 3, '/sys/notice/log.json', 3, 1, 23, 'Audit trail for changes', 'Admin', '2016-08-15 14:00:00', '127.0.0.1'),
                                                                                                                                                ('201608150024', 'Check Publish Status', 3, '/sys/notice/status.json', 3, 1, 24, 'System status check', 'Admin', '2016-08-15 14:05:00', '127.0.0.1'),
                                                                                                                                                ('201608150025', 'Add Category', 3, '/sys/notice/saveCategory.json', 2, 1, 25, 'Add new notice type', 'Admin', '2016-08-15 15:00:00', '127.0.0.1'),
                                                                                                                                                ('201608150026', 'Delete Category', 3, '/sys/notice/delCategory.json', 2, 1, 26, 'Remove unused type', 'Admin', '2016-08-15 15:00:00', '127.0.0.1'),
                                                                                                                                                ('201608150027', 'Update Template', 3, '/sys/notice/template.json', 2, 1, 27, 'Edit standard notice layout', 'Admin', '2016-08-15 15:10:00', '127.0.0.1'),
                                                                                                                                                ('201608150028', 'Send Urgent SMS', 3, '/sys/notice/sendSms.json', 2, 1, 28, 'Trigger phone alert', 'Admin', '2016-08-15 16:00:00', '127.0.0.1'),
                                                                                                                                                ('201608150029', 'Trigger Pop-up', 3, '/sys/notice/popup.json', 2, 1, 29, 'Force display on login', 'Admin', '2016-08-15 16:00:00', '127.0.0.1'),
                                                                                                                                                ('201608150030', 'Archive Old Notices', 3, '/sys/notice/archive.json', 2, 1, 30, 'Clean up main list', 'Admin', '2016-08-15 16:10:00', '127.0.0.1'),
                                                                                                                                                ('201608150031', 'Export Notice Data', 3, '/sys/notice/export.json', 2, 1, 31, 'Download record summary', 'Admin', '2016-08-15 17:00:00', '127.0.0.1'),
                                                                                                                                                ('201608150032', 'Batch Delete Drafts', 3, '/sys/notice/batchDelDraft.json', 2, 1, 32, 'Bulk cleanup', 'Admin', '2016-08-15 17:05:00', '127.0.0.1'),
                                                                                                                                                ('201608150033', 'Batch Publish', 3, '/sys/notice/batchPublish.json', 2, 1, 33, 'Release multiple items', 'Admin', '2016-08-15 17:05:00', '127.0.0.1'),
                                                                                                                                                ('201608150034', 'Force Close Alert', 3, '/sys/notice/forceClose.json', 2, 1, 34, 'Emergency shutdown of notice', 'Admin', '2016-08-15 17:10:00', '127.0.0.1'),
                                                                                                                                                ('201608150035', 'System Global Notice', 3, '/sys/notice/global.json', 3, 1, 35, 'Full system-wide broadcast', 'Admin', '2016-08-15 17:15:00', '127.0.0.1'),
                                                                                                                                                ('201608150036', 'Push to Mobile App', 3, '/sys/notice/pushApp.json', 2, 1, 36, 'Mobile notification trigger', 'Admin', '2016-08-15 18:00:00', '127.0.0.1'),
                                                                                                                                                ('201608150037', 'Manage Comment Thread', 3, '/sys/notice/comments.page', 1, 1, 37, 'Interactive notice feedback', 'Admin', '2016-08-15 18:00:00', '127.0.0.1'),
                                                                                                                                                ('201608150038', 'Delete Comment', 3, '/sys/notice/delComment.json', 2, 1, 38, 'Moderate user feedback', 'Admin', '2016-08-15 18:05:00', '127.0.0.1'),
                                                                                                                                                ('201608150039', 'Update Display Order', 3, '/sys/notice/sort.json', 2, 1, 39, 'Manual priority adjustment', 'Admin', '2016-08-15 18:10:00', '127.0.0.1'),
                                                                                                                                                ('201608150040', 'View Reach Report', 3, '/sys/notice/reachReport.json', 3, 1, 40, 'Analytic overview of coverage', 'Admin', '2016-08-15 18:15:00', '127.0.0.1'),
                                                                                                                                                ('201608150041', 'Set High Priority Icon', 3, '/sys/notice/setIcon.json', 2, 1, 41, 'Visual UI marking', 'Admin', '2016-08-15 18:20:00', '127.0.0.1'),
                                                                                                                                                ('201608150042', 'Notify Specific Department', 3, '/sys/notice/deptNotify.json', 2, 1, 42, 'Targeted internal comms', 'Admin', '2016-08-15 18:25:00', '127.0.0.1'),
                                                                                                                                                ('201608150043', 'Clone Existing Notice', 3, '/sys/notice/clone.json', 2, 1, 43, 'Duplicate content for reuse', 'Admin', '2016-08-15 18:30:00', '127.0.0.1'),
                                                                                                                                                ('201608150044', 'Internal API: Notify Audit', 3, '/api/notice/notifyAudit', 3, 1, 44, 'Service trigger for compliance', 'Admin', '2016-08-15 18:35:00', '127.0.0.1'),
                                                                                                                                                ('201608150045', 'Access Notice Archive Page', 3, '/sys/notice/archive.page', 1, 1, 45, 'Historical records entry', 'Admin', '2016-08-15 18:40:00', '127.0.0.1');




INSERT INTO `sys_acl` (`code`, `name`, `acl_module_id`, `url`, `type`, `status`, `seq`, `remark`, `operator`, `operate_time`, `operate_ip`) VALUES
-- 1. System Navigation (Type 1: Menu)
('201611200001', 'ACL Module Management Page', 6, '/sys/aclModule/aclModule.page', 1, 1, 1, 'Manage the permission tree modules', 'Admin', '2016-11-20 09:00:00', '127.0.0.1'),
('201611200002', 'ACL Detail Management Page', 6, '/sys/acl/acl.page', 1, 1, 2, 'Entry point for defining permissions', 'Admin', '2016-11-20 09:00:00', '127.0.0.1'),
('201611200003', 'Security Audit Dashboard', 6, '/sys/acl/audit.page', 1, 1, 3, 'Overview of system-wide access logs', 'Admin', '2016-11-20 09:00:00', '127.0.0.1'),
('201611200004', 'Global Permission Search', 6, '/sys/acl/search.page', 1, 1, 4, 'Search across all modules', 'Admin', '2016-11-20 09:00:00', '127.0.0.1'),

-- 2. Module Administration (Type 2: Button)
('201611200005', 'Save ACL Module', 6, '/sys/aclModule/save.json', 2, 1, 5, 'Create a new permission category', 'Admin', '2016-11-20 09:05:00', '127.0.0.1'),
('201611200006', 'Update ACL Module', 6, '/sys/aclModule/update.json', 2, 1, 6, 'Modify existing module metadata', 'Admin', '2016-11-20 09:05:00', '127.0.0.1'),
('201611200007', 'Delete ACL Module', 6, '/sys/aclModule/delete.json', 2, 1, 7, 'Remove module and its children', 'Admin', '2016-11-20 09:10:00', '127.0.0.1'),
('201611200008', 'Query ACL Module Tree', 6, '/sys/aclModule/tree.json', 2, 1, 8, 'Fetch hierarchical module structure', 'Admin', '2016-11-20 09:10:00', '127.0.0.1'),

-- 3. ACL Record Management (Type 2: Button)
('201611200009', 'Create New Permission', 6, '/sys/acl/save.json', 2, 1, 9, 'Define a new URL/Code/Type record', 'Admin', '2016-11-20 10:00:00', '127.0.0.1'),
('201611200010', 'Update Permission Detail', 6, '/sys/acl/update.json', 2, 1, 10, 'Edit existing ACL configuration', 'Admin', '2016-11-20 10:00:00', '127.0.0.1'),
('201611200011', 'Batch Delete ACLs', 6, '/sys/acl/batchDelete.json', 2, 1, 11, 'Bulk cleanup of permissions', 'Admin', '2016-11-20 10:05:00', '127.0.0.1'),
('201611200012', 'Freeze Permission', 6, '/sys/acl/status.json', 2, 1, 12, 'Set status to 0 (Frozen)', 'Admin', '2016-11-20 10:05:00', '127.0.0.1'),
('201611200013', 'Query ACL List by Module', 6, '/sys/acl/page.json', 2, 1, 13, 'Filter permissions by parent ID', 'Admin', '2016-11-20 10:10:00', '127.0.0.1'),

-- 4. Cache & Distributed Auth Governance (Type 3: API/Other)
('201611200014', 'Refresh Redis ACL Cache', 6, '/sys/acl/refreshCache.json', 3, 1, 14, 'Manually trigger cache rebuild', 'Admin', '2016-11-20 11:00:00', '127.0.0.1'),
('201611200015', 'Validate ACL Token', 6, '/api/acl/validate', 3, 1, 15, 'Internal check for distributed requests', 'Admin', '2016-11-20 11:00:00', '127.0.0.1'),
('201611200016', 'Check Resource Availability', 6, '/api/acl/checkResource', 3, 1, 16, 'Check if URL is currently protected', 'Admin', '2016-11-20 11:05:00', '127.0.0.1'),

-- 5. Auditing & Logging
('201611200017', 'View ACL Change Log', 6, '/sys/acl/log.json', 2, 1, 17, 'Full audit trail of who changed what', 'Admin', '2016-11-20 13:00:00', '127.0.0.1'),
('201611200018', 'Export ACL Inventory', 6, '/sys/acl/export.json', 2, 1, 18, 'Download full system permission map', 'Admin', '2016-11-20 13:00:00', '127.0.0.1'),
('201611200019', 'Trace User Permissions', 6, '/sys/acl/traceUser.json', 2, 1, 19, 'Debug why a user has access', 'Admin', '2016-11-20 13:10:00', '127.0.0.1'),

-- 6. Batch Configuration & UI Control
('201611200020', 'Batch Update ACL Module', 6, '/sys/aclModule/batchUpdate.json', 2, 1, 20, 'Bulk move ACLs across modules', 'Admin', '2016-11-20 14:00:00', '127.0.0.1'),
('201611200021', 'Set ACL Sort Order', 6, '/sys/acl/seq.json', 2, 1, 21, 'Manually adjust UI sequence', 'Admin', '2016-11-20 14:00:00', '127.0.0.1'),
('201611200022', 'Clear System Breadcrumbs', 6, '/sys/acl/clearNav.json', 3, 1, 22, 'UI cache clearing', 'Admin', '2016-11-20 14:10:00', '127.0.0.1'),

-- 7. Advanced Security Config
('201611200023', 'Manage Regex White-lists', 6, '/sys/acl/whitelist.json', 2, 1, 23, 'URLs that bypass auth checks', 'Admin', '2016-11-20 15:00:00', '127.0.0.1'),
('201611200024', 'IP Restriction Setup', 6, '/sys/acl/ipRange.json', 2, 1, 24, 'Restrict ACL access by IP ranges', 'Admin', '2016-11-20 15:00:00', '127.0.0.1'),
('201611200025', 'Force Sync MyBatis Mappers', 6, '/sys/acl/syncMappers.json', 3, 1, 25, 'Reload SQL mappings for ACL checks', 'Admin', '2016-11-20 15:10:00', '127.0.0.1'),

-- 8. Integration & Monitoring
('201611200026', 'AOP Logger Config', 6, '/sys/acl/aopConfig.json', 2, 1, 26, 'Adjust logging levels for ACL AOP', 'Admin', '2016-11-20 16:00:00', '127.0.0.1'),
('201611200027', 'Security Pulse Check', 6, '/api/acl/health', 3, 1, 27, 'Monitor distributed auth health', 'Admin', '2016-11-20 16:00:00', '127.0.0.1'),
('201611200028', 'Lock ACL Table', 6, '/sys/acl/lock.json', 3, 1, 28, 'Prevent concurrent modifications', 'Admin', '2016-11-20 16:10:00', '127.0.0.1'),
('201611200029', 'Unlock ACL Table', 6, '/sys/acl/unlock.json', 3, 1, 29, 'Release management lock', 'Admin', '2016-11-20 16:10:00', '127.0.0.1'),

-- 9. Administrative Overrides
('201611200030', 'Reset Master Code', 6, '/sys/acl/resetMaster.json', 3, 1, 30, 'Emergency security reset', 'Admin', '2016-11-20 17:00:00', '127.0.0.1'),
('201611200031', 'Access Legacy Permissions', 6, '/sys/acl/legacy.page', 1, 1, 31, 'View compatibility layer', 'Admin', '2016-11-20 17:00:00', '127.0.0.1'),
('201611200032', 'Batch Move Permissions', 6, '/sys/acl/move.json', 2, 1, 32, 'Relocate ACLs between modules', 'Admin', '2016-11-20 17:05:00', '127.0.0.1'),
('201611200033', 'Generate ACL Graph', 6, '/sys/acl/graph.json', 2, 1, 33, 'Visual map of the permission tree', 'Admin', '2016-11-20 17:05:00', '127.0.0.1'),

-- 10. Metadata & Extended Governance
('201611200034', 'Edit ACL Remarks', 6, '/sys/acl/remark.json', 2, 1, 34, 'Update business justifications', 'Admin', '2016-11-20 17:10:00', '127.0.0.1'),
('201611200035', 'Archive Old ACL Modules', 6, '/sys/aclModule/archive.json', 2, 1, 35, 'Clean up unused modules', 'Admin', '2016-11-20 17:15:00', '127.0.0.1'),
('201611200036', 'Recover Deleted ACL', 6, '/sys/acl/recover.json', 2, 1, 36, 'Undo accidental deletion', 'Admin', '2016-11-20 17:15:00', '127.0.0.1'),
('201611200037', 'Sync Permissions to LDAP', 6, '/sys/acl/ldapSync.json', 2, 1, 37, 'External directory integration', 'Admin', '2016-11-20 17:20:00', '127.0.0.1'),
('201611200038', 'Check Circular References', 6, '/sys/aclModule/checkCycle.json', 3, 1, 38, 'Integrity check for module tree', 'Admin', '2016-11-20 17:20:00', '127.0.0.1'),
('201611200039', 'Update Module Sequence', 6, '/sys/aclModule/seq.json', 2, 1, 39, 'Sort modules in sidebar', 'Admin', '2016-11-20 17:25:00', '127.0.0.1'),
('201611200040', 'View Permission Dependencies', 6, '/sys/acl/deps.json', 3, 1, 40, 'Find related ACL requirements', 'Admin', '2016-11-20 17:25:00', '127.0.0.1'),
('201611200041', 'Generate Monthly Audit PDF', 6, '/sys/acl/auditReport.json', 2, 1, 41, 'Managerial compliance report', 'Admin', '2016-11-20 17:30:00', '127.0.0.1'),
('201611200042', 'Lock Global ACL status', 6, '/sys/acl/globalLock.json', 3, 1, 42, 'Maintenance mode toggle', 'Admin', '2016-11-20 17:30:00', '127.0.0.1'),
('201611200043', 'Clear Request Tracking', 6, '/sys/acl/clearTrack.json', 3, 1, 43, 'Performance cleanup', 'Admin', '2016-11-20 17:35:00', '127.0.0.1'),
('201611200044', 'Internal API: Notify DevTeam', 6, '/api/acl/notifyDev', 3, 1, 44, 'Alert on unauthorized access spikes', 'Admin', '2016-11-20 17:35:00', '127.0.0.1'),
('201611200045', 'View Permissions Stats', 6, '/sys/acl/stats.page', 1, 1, 45, 'Visual breakdown of ACL usage', 'Admin', '2016-11-20 17:40:00', '127.0.0.1');




INSERT INTO `sys_acl` (`code`, `name`, `acl_module_id`, `url`, `type`, `status`, `seq`, `remark`, `operator`, `operate_time`, `operate_ip`) VALUES
-- 1. Dashboard & Monitoring (Type 1: Menu)
('201612150001', 'System Health Dashboard', 5, '/sys/ops/health.page', 1, 1, 1, 'Real-time server status overview', 'Admin', '2016-12-15 08:00:00', '127.0.0.1'),
('201612150002', 'Job Scheduler Page', 5, '/sys/ops/jobs.page', 1, 1, 2, 'Manage background Quartz/Spring tasks', 'Admin', '2016-12-15 08:00:00', '127.0.0.1'),
('201612150003', 'Redis Monitor Page', 5, '/sys/ops/redis.page', 1, 1, 3, 'Cache hits and memory usage stats', 'Admin', '2016-12-15 08:00:00', '127.0.0.1'),
('201612150004', 'JVM Metrics Page', 5, '/sys/ops/jvm.page', 1, 1, 4, 'Garbage collection and heap monitoring', 'Admin', '2016-12-15 08:00:00', '127.0.0.1'),

-- 2. Task & Job Controls (Type 2: Button)
('201612150005', 'Execute Manual Job', 5, '/sys/ops/runJob.json', 2, 1, 5, 'Trigger background task immediately', 'Admin', '2016-12-15 08:05:00', '127.0.0.1'),
('201612150006', 'Pause Scheduler', 5, '/sys/ops/pauseJob.json', 2, 1, 6, 'Suspend all recurring tasks', 'Admin', '2016-12-15 08:05:00', '127.0.0.1'),
('201612150007', 'Resume Scheduler', 5, '/sys/ops/resumeJob.json', 2, 1, 7, 'Restart suspended tasks', 'Admin', '2016-12-15 08:10:00', '127.0.0.1'),
('201612150008', 'Update Cron Expression', 5, '/sys/ops/updateCron.json', 2, 1, 8, 'Modify task frequency', 'Admin', '2016-12-15 08:10:00', '127.0.0.1'),
('201612150009', 'View Job Execution Log', 5, '/sys/ops/jobLog.json', 2, 1, 9, 'Check pass/fail status of tasks', 'Admin', '2016-12-15 08:15:00', '127.0.0.1'),

-- 3. Cache & Database Operations
('201612150010', 'Flush Redis Database', 5, '/sys/ops/flushRedis.json', 2, 1, 10, 'Clear all distributed cache data', 'Admin', '2016-12-15 09:00:00', '127.0.0.1'),
('201612150011', 'Delete Specific Cache Key', 5, '/sys/ops/delCache.json', 2, 1, 11, 'Targeted cache invalidation', 'Admin', '2016-12-15 09:00:00', '127.0.0.1'),
('201612150012', 'Optimize DB Tables', 5, '/sys/ops/dbOptimize.json', 2, 1, 12, 'Run maintenance on MyBatis tables', 'Admin', '2016-12-15 09:05:00', '127.0.0.1'),
('201612150013', 'View Slow SQL Logs', 5, '/sys/ops/slowSql.json', 2, 1, 13, 'Identify performance bottlenecks', 'Admin', '2016-12-15 09:10:00', '127.0.0.1'),

-- 4. Infrastructure & Integration (Type 3: API)
('201612150014', 'API: Heartbeat Check', 5, '/api/ops/heartbeat', 3, 1, 14, 'External load balancer ping', 'Admin', '2016-12-15 10:00:00', '127.0.0.1'),
('201612150015', 'API: Sync Config Servers', 5, '/api/ops/syncConfig', 3, 1, 15, 'Push updates to distributed nodes', 'Admin', '2016-12-15 10:00:00', '127.0.0.1'),
('201612150016', 'Check Disk Usage', 5, '/api/ops/diskCheck', 3, 1, 16, 'Server storage monitoring', 'Admin', '2016-12-15 10:05:00', '127.0.0.1'),

-- 5. Deployment & Versioning
('201612150017', 'Access Deployment Page', 5, '/sys/ops/deploy.page', 1, 1, 17, 'Manual package deployment entry', 'Admin', '2016-12-15 11:00:00', '127.0.0.1'),
('201612150018', 'Upload Patch File', 5, '/sys/ops/uploadPatch.json', 2, 1, 18, 'Hotfix application', 'Admin', '2016-12-15 11:00:00', '127.0.0.1'),
('201612150019', 'Rollback Version', 5, '/sys/ops/rollback.json', 2, 1, 19, 'Revert to previous stable build', 'Admin', '2016-12-15 11:05:00', '127.0.0.1'),
('201612150020', 'View Release Notes', 5, '/sys/ops/releaseNotes.json', 2, 1, 20, 'Internal documentation access', 'Admin', '2016-12-15 11:10:00', '127.0.0.1'),

-- 6. Log Management
('201612150021', 'Download Error Logs', 5, '/sys/ops/errorLog.json', 2, 1, 21, 'Batch fetch system stack traces', 'Admin', '2016-12-15 13:00:00', '127.0.0.1'),
('201612150022', 'Clean Old Logs', 5, '/sys/ops/clearLog.json', 2, 1, 22, 'Rotate and delete historical logs', 'Admin', '2016-12-15 13:00:00', '127.0.0.1'),
('201612150023', 'Set Log Level', 5, '/sys/ops/logLevel.json', 2, 1, 23, 'Dynamic change to DEBUG/INFO', 'Admin', '2016-12-15 13:05:00', '127.0.0.1'),

-- 7. Security Operations
('201612150024', 'Block Malicious IP', 5, '/sys/ops/blockIp.json', 2, 1, 24, 'Firewall-level restriction', 'Admin', '2016-12-15 14:00:00', '127.0.0.1'),
('201612150025', 'Unblock IP Address', 5, '/sys/ops/unblockIp.json', 2, 1, 25, 'Remove IP from blacklist', 'Admin', '2016-12-15 14:00:00', '127.0.0.1'),
('201612150026', 'Scan For Vulnerabilities', 5, '/sys/ops/securityScan.json', 2, 1, 26, 'Trigger automated security audit', 'Admin', '2016-12-15 14:10:00', '127.0.0.1'),

-- 8. Communication & Messaging
('201612150027', 'Mail Server Config', 5, '/sys/ops/mailConfig.page', 1, 1, 27, 'SMTP settings management', 'Admin', '2016-12-15 15:00:00', '127.0.0.1'),
('201612150028', 'Send Test Email', 5, '/sys/ops/testMail.json', 2, 1, 28, 'Verify SMTP connectivity', 'Admin', '2016-12-15 15:00:00', '127.0.0.1'),
('201612150029', 'SMS Gateway Status', 5, '/sys/ops/smsStatus.json', 2, 1, 29, 'Check third-party provider health', 'Admin', '2016-12-15 15:10:00', '127.0.0.1'),

-- 9. System Administration
('201612150030', 'Edit Global Properties', 5, '/sys/ops/properties.page', 1, 1, 30, 'Modify application.properties UI', 'Admin', '2016-12-15 16:00:00', '127.0.0.1'),
('201612150031', 'Update System Banner', 5, '/sys/ops/banner.json', 2, 1, 31, 'Modify login screen message', 'Admin', '2016-12-15 16:00:00', '127.0.0.1'),
('201612150032', 'Maintenance Mode Toggle', 5, '/sys/ops/maintenance.json', 2, 1, 32, 'Switch system to read-only', 'Admin', '2016-12-15 16:10:00', '127.0.0.1'),
('201612150033', 'Force Logout All Users', 5, '/sys/ops/kickAll.json', 2, 1, 33, 'Emergency session termination', 'Admin', '2016-12-15 16:10:00', '127.0.0.1'),

-- 10. Performance & Optimization
('201612150034', 'Thread Pool Monitor', 5, '/sys/ops/threads.json', 3, 1, 34, 'View active worker threads', 'Admin', '2016-12-15 17:00:00', '127.0.0.1'),
('201612150035', 'Connection Pool Stats', 5, '/sys/ops/dbPool.json', 3, 1, 35, 'Monitor Druid/HikariCP health', 'Admin', '2016-12-15 17:00:00', '127.0.0.1'),
('201612150036', 'Warm Up Cache', 5, '/sys/ops/warmup.json', 2, 1, 36, 'Pre-load critical data to Redis', 'Admin', '2016-12-15 17:05:00', '127.0.0.1'),
('201612150037', 'Access Reports Page', 5, '/sys/ops/reports.page', 1, 1, 37, 'Entry for system performance docs', 'Admin', '2016-12-15 17:05:00', '127.0.0.1'),
('201612150038', 'Generate Uptime Report', 5, '/sys/ops/uptime.json', 2, 1, 38, 'Service availability summary', 'Admin', '2016-12-15 17:10:00', '127.0.0.1'),
('201612150039', 'Analyze Memory Leak', 5, '/sys/ops/leakCheck.json', 3, 1, 39, 'Run heap dump analysis', 'Admin', '2016-12-15 17:15:00', '127.0.0.1'),
('201612150040', 'Manage API Quotas', 5, '/sys/ops/quotas.page', 1, 1, 40, 'Rate limiting configuration UI', 'Admin', '2016-12-15 17:15:00', '127.0.0.1'),
('201612150041', 'Update Rate Limits', 5, '/sys/ops/setQuotas.json', 2, 1, 41, 'Modify request per second caps', 'Admin', '2016-12-15 17:20:00', '127.0.0.1'),
('201612150042', 'View Active Sessions', 5, '/sys/ops/sessions.json', 3, 1, 42, 'Real-time user count tracking', 'Admin', '2016-12-15 17:20:00', '127.0.0.1'),
('201612150043', 'Restart Specific Service', 5, '/sys/ops/restartSvc.json', 2, 1, 43, 'Microservice instance reboot', 'Admin', '2016-12-15 17:25:00', '127.0.0.1'),
('201612150044', 'Internal API: Notify OpsTeam', 5, '/api/ops/notifyAlert', 3, 1, 44, 'Push alert to pager/chat', 'Admin', '2016-12-15 17:30:00', '127.0.0.1'),
('201612150045', 'Archive Ops History', 5, '/sys/ops/archiveOps.json', 2, 1, 45, 'Clean up historical health data', 'Admin', '2016-12-15 17:35:00', '127.0.0.1');