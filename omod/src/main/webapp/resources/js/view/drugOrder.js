define(
[
	openhmis.url.backboneBase + 'js/lib/jquery',
	openhmis.url.backboneBase + 'js/lib/underscore',
	openhmis.url.backboneBase + 'js/lib/backbone',
	openhmis.url.backboneBase + 'js/view/generic',
	'link!' + openhmis.url.pharmacyBase + 'css/style.css',
	openhmis.url.backboneBase + 'js/lib/i18n',
	openhmis.url.backboneBase + 'js/lib/backbone-forms',
	openhmis.url.backboneBase + 'js/view/editors'
],
function($, _, Backbone, openhmis) {
	openhmis.DrugOrderEntryItemView = openhmis.GenericListItemView.extend({
		initialize: function(options) {
			_.bindAll(this, "expandInstructions", "collapseInstructions");
			openhmis.GenericListItemView.prototype.initialize.call(this, options);
			this.form.on("instructions:focus", this.expandInstructions);
			this.form.on("instructions:blur", this.collapseInstructions);
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
		
		render: function() {
			openhmis.GenericListItemView.prototype.render.call(this);
			return this;
		}
	});
	
	openhmis.DrugOrderEntryView = openhmis.GenericListEntryView.extend({
		className: "drugOrderEntry",
		initialize: function(options) {
			openhmis.GenericListView.prototype.initialize.call(this, options);
			this.itemView = openhmis.DrugOrderEntryItemView;
		},
		
		//schema: {
		//	"drug": { type: "Autocomplete", options: new openhmis.GenericCollection([], { model: openhmis.Department }) }
		//},
		
		render: function() {
			openhmis.GenericListEntryView.prototype.render.call(this, { options: { listTitle: "Patient's Prescriptions" }});
			return this;
		}
	});
});