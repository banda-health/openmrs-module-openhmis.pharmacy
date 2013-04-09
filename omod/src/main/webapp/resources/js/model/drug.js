define(
[
	openhmis.url.backboneBase + 'js/model/generic',
	openhmis.url.backboneBase + 'js/lib/i18n'
],
function(openhmis, __) {
	openhmis.Drug = openhmis.GenericModel.extend({
		meta: {
			name: "Drug",
			restUrl: "v1/drug"
		},
		
		schema: {
			name: { type: "Text" },
			description: { type: "Text" },
			dosageForm: { type: "Object" },
			doseStrength: { type: "Number" },
			maximumDailyDose: { type: "Number" },
			minimumDailyDose: { type: "Number" },
			units: { type: "Text" },
			concept: { type: "Object" },
			combination: { type: "Checkbox" },
			route: { type: "Object" }
		}
	});
	
	openhmis.DrugOrder = openhmis.GenericModel.extend({
		meta: {
			name: "Drug Order"
		},
		
		schema: {
			drug: { type: "NestedModel", model: openhmis.Drug, objRef: true },
			daw: { type: "Checkbox", title: "DAW" },
			frequency: { type: "Text" },
			prn: { type: "Checkbox", title: "PRN" },
			duration: { type: "Text" },
			quantity: { type: "Number" },
			instructions: { type: "TextArea"}
		},
		
		validate: function(attrs) {
			if (!attrs.drug || !attrs.drug.id) return { drug: __("Please choose a drug") };
			return null;
		}
	});
});