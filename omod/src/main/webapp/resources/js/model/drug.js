/*
 * The contents of this file are subject to the OpenMRS Public License
 * Version 2.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://license.openmrs.org
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations
 * under the License.
 *
 * Copyright (C) OpenMRS, LLC.  All Rights Reserved.
 */
define(
[
	openhmis.url.backboneBase + 'js/model/generic',
	openhmis.url.backboneBase + 'js/lib/i18n',
	openhmis.url.backboneBase + 'js/model/patient'
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
			name: "Drug Order",
			restUrl: "v2/pharmacy/order"
		},
		
		schema: {
			patient: { type: "NestedModel", model: openhmis.Patient, objRef: true },
			drug: { type: "NestedModel", model: openhmis.Drug, objRef: true },
			daw: { type: "Checkbox", title: "DAW" },
			frequency: { type: "Text" },
			prn: { type: "Checkbox", title: "PRN" },
			duration: { type: "Text" },
			quantity: { type: "Number" },
			instructions: { type: "TextArea"},
			type: { type: "Text", hidden: true }
		},
		
		initialize: function(attrs, options) {
			openhmis.GenericModel.prototype.initialize.call(this, attrs, options);
			if (!this.get("type"))
				this.set("type", "drugorder", { silent: true });
		},
		
		validate: function(attrs) {
			//if (!attrs.patient || !attrs.patient.id) return { patient: __("A DrugOrder must be associated with a Patient.") };
			if (!attrs.drug) return { drug: __("Please choose a drug.") }
			if (!attrs.frequency) return { frequency: __("Please specify how often this drug should be taken.") }
			
			return null;
		},
		
		toJSON: function() {
			var attrs = openhmis.GenericModel.prototype.toJSON.call(this);
			delete attrs.duration;
			return attrs;
		}
	});
});