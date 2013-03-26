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
		var drugOrderListView = new openhmis.DrugOrderListView({
			model: drugOrderList,
			showRetiredOption: false,
			showPaging: false,
			itemActions: ["inlineEdit", "remove"]
		});
		
		$("#content").append(drugOrderListView.el);
		drugOrderListView.render();
		drugOrderListView.model.add(new openhmis.DrugOrder({
			drug: "Amoxicillin 250mg",
			frequency: "3/day",
			duration: "10 days",
			instructions: "Take with food and water.\nDo not take with jelly beans."
		}));
	});
});