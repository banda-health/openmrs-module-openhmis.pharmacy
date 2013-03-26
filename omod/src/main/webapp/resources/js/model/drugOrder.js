define(
[
	openhmis.url.backboneBase + 'js/model/generic'
],
function(openhmis) {
	openhmis.DrugOrder = openhmis.GenericModel.extend({
		meta: {
			name: "Drug Order",
			namePlural: "Drug Orders"
		},
		
		schema: {
			drug: { type: "Text" },
			frequency: { type: "Text" },
			prn: { type: "Checkbox" },
			duration: { type: "Text" },
			instructions: { type: "TextArea"}
		}
	});
});