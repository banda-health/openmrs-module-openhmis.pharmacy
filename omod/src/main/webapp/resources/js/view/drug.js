define(
[
	openhmis.url.backboneBase + 'js/lib/jquery',
	openhmis.url.backboneBase + 'js/lib/underscore',
	openhmis.url.backboneBase + 'js/lib/backbone',
	openhmis.url.backboneBase + 'js/view/generic',
	openhmis.url.backboneBase + 'js/lib/i18n',
	'link!' + openhmis.url.pharmacyBase + 'css/style.css',
	openhmis.url.backboneBase + 'js/lib/backbone-forms',
	openhmis.url.pharmacyBase + 'js/view/editors',
	openhmis.url.pharmacyBase + 'js/model/drug'
],
function($, _, Backbone, openhmis, __) {
	openhmis.DrugOrderEntryItemView = openhmis.GenericListItemView.extend({
		initialize: function(options) {
			_.bindAll(this, "expandInstructions", "collapseInstructions");
			openhmis.GenericListItemView.prototype.initialize.call(this, options);
			this.form.on("instructions:focus", this.expandInstructions);
			this.form.on("instructions:blur", this.collapseInstructions);
			this.form.on("drug:select", this.onSelectDrug);
			this.form.on("frequency:change", this.updateQuantity);
			this.form.on("duration:change", this.updateQuantity);
		},
		
		expandInstructions: function(event) {
			$instructEditor = this.$("td.field-instructions .editor");
			$instructEditor.css("overflow", "visible");
			$instructEditor.find("textarea").css("z-index", "999");
			$instructEditor.find("textarea").stop().animate({ height: "5em" }, 200, function() {});
		},
		
		collapseInstructions: function(event) {
			$instructEditor = this.$("td.field-instructions .editor");
			var self = this;
			$instructEditor.find("textarea").stop().animate({ height: "100%" }, 100, function() {
				$instructEditor = self.$("td.field-instructions .editor")
				$instructEditor.css("overflow", "hidden");
				$instructEditor.find("textarea").css("z-index", "1");
			});
		},
		
		updateQuantity: function(event) {
			var frequencyModel = this.form.getValue("frequency");
			try { var frequency = frequencyModel.get("value") }
			catch (e) { /* Abort if frequencyModel is not a model */ return }
			var duration = this.form.fields["duration"].editor.getDays();
			if (frequency && duration)
				this.form.setValue("quantity", frequency * duration);
		},
		
		render: function() {
			openhmis.GenericListItemView.prototype.render.call(this);
			return this;
		}
	});
	
	openhmis.QxHDrugFrequency = openhmis.GenericModel.extend({
		initialize: function(options) {
			this.on("change:display", this.updateValue);
		},		
		updateValue: function() {
			var everyXHours = parseInt(this.get("display").charAt(1));
			if (isNaN(everyXHours))
				this.set("value", this.get("display"));
			else
				this.set("value", Math.floor(24 / everyXHours));
		}
	});
	
	openhmis.DrugOrderEntryView = openhmis.GenericListEntryView.extend({
		id: "prescriptions",
		className: "drugOrderEntry",
		initialize: function(options) {
			openhmis.GenericListView.prototype.initialize.call(this, options);
			this.itemView = openhmis.DrugOrderEntryItemView;
			this.patient = null;
		},
		
		schema: {
			patient: { type: Object, hidden: true, objRef: true },
			drug: { type: "Autocomplete", options: new openhmis.GenericCollection([], { model: openhmis.Drug }) },
			frequency: { type: "Autocomplete", minLength: 1, options: new Backbone.Collection([
				new openhmis.GenericModel({ display: "1/day", value: 1 }),
				new openhmis.GenericModel({ display: "QD", value: 1 }),
				new openhmis.GenericModel({ display: "OD", value: 1 }),
				new openhmis.GenericModel({ display: "2/day", value: 2 }),
				new openhmis.GenericModel({ display: "BID", value: 2 }),
				new openhmis.GenericModel({ display: "3/day", value: 3 }),
				new openhmis.GenericModel({ display: "TID", value: 3 }),
				new openhmis.GenericModel({ display: "4/day", value: 4 }),
				new openhmis.GenericModel({ display: "QID", value: 4 }),
				new openhmis.GenericModel({ display: "PRN", value: "PRN" }),
				new openhmis.GenericModel({ display: "QHS", value: 1 })
			])},
			prn: { type: "Checkbox", title: "PRN", hidden: true },
			duration: { type: "Duration", minLength: 1 },
		},
		
		setPatient: function(patient) {
			this.patient = patient;
		},
		
		validate: function() {
			var errors;
			var orders = this.model.models;
			for (var o in orders) {
				if (orders[o].view.commitForm() !== undefined)
					return false;
			}
			try {
				var newItemView = this.newItem.view;
				if (newItemView.form.getValue("drug") && newItemView.commitForm() !== undefined)
					return false;
			} catch(e) {
				/* If something fails here it's likely that there's not much
				 * data in the new item, so don't worry about it */
			}
			var errorMap = {};
			var elMap = {
				'lineItems': [ $('#prescriptions'), this ],
				'patient': [ $('#patient-view'),  $('#inputNode') ]
			}
			if (!this.patient)
				errorMap["patient"] = __("Prescriptions must be associated with a patient.");
			if (this.model.length === 0)
				errorMap["lineItems"] = __("Please enter a prescription.");
			for (var e in errorMap) {
				openhmis.validationMessage(elMap[e][0], errorMap[e], elMap[e][1]);
				return false;
			}
			return true;
		},
		
		save: function(event) {
			if (!this.validate()) return false;
			
			return true;
		},
		
		render: function() {
			openhmis.GenericListEntryView.prototype.render.call(this, { options: { listTitle: "Patient's Prescriptions" }});
			return this;
		}
	});
});