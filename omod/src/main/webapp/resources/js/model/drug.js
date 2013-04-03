define(
[
	openhmis.url.backboneBase + 'js/model/generic'
],
function(openhmis) {
	openhmis.DrugOrder = openhmis.GenericModel.extend({
		meta: {
			name: "Drug Order"
		},
		
		schema: {
			drug: { type: "Text" },
			daw: { type: "Checkbox", title: "DAW" },
			frequency: { type: "Text" },
			prn: { type: "Checkbox", title: "PRN" },
			duration: { type: "Text" },
			instructions: { type: "TextArea"}
		}
	});
});