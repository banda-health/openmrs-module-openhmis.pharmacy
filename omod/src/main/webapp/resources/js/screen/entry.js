curl(
{ baseUrl: openhmis.url.resources },
[
	openhmis.url.backboneBase + 'js/lib/jquery',
	openhmis.url.backboneBase + 'js/openhmis',
	openhmis.url.pharmacyBase + 'js/model/drugOrder',
	openhmis.url.pharmacyBase + 'js/view/drugOrder'
],
function($, openhmis) {
	$(function() {
		var drugOrderList = new openhmis.GenericCollection([], { model: openhmis.DrugOrder });
		var drugOrderEntryView = new openhmis.DrugOrderEntryView({
			model: drugOrderList,
			showRetiredOption: false,
			showPaging: false,
			itemActions: ["inlineEdit", "remove"]
		});
		
		$("#content").append(drugOrderEntryView.el);
		drugOrderEntryView.render();
		drugOrderEntryView.setupNewItem();
	});
});