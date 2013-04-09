define(
[
	openhmis.url.backboneBase + 'js/lib/jquery',
	openhmis.url.backboneBase + 'js/lib/backbone',
	openhmis.url.backboneBase + 'js/lib/underscore',
	openhmis.url.backboneBase + 'js/lib/i18n',
	openhmis.url.backboneBase + 'js/lib/backbone-forms',
	openhmis.url.backboneBase + 'js/view/editors',
	openhmis.url.backboneBase + 'js/lib/labelOver',
],
function($, Backbone, _, __) {
	var editors = Backbone.Form.editors;
	
	editors.Duration = editors.Autocomplete.extend({
		tagName: "span",
		className: "duration",
		tmplFile: openhmis.url.pharmacyBase + 'template/editors.html',
		tmplSelector: "#duration",
		initialize: function(options) {
			_.bindAll(this);
			this.schema = options.schema || {};
			if (this.schema.options === undefined)
				this.schema.options = this.suggestDuration;
			editors.Autocomplete.prototype.initialize.call(this, options);
			this.cache = {};
		},
		
		durationTypes: [
			"day",
			"week",
			"month"
		],
		
		suggestDuration: function() {
			var suggestions = [];
			if (arguments.length === 2) {
				var input = arguments[0].term;
				var resultCallback = arguments[1];
				var matches = openhmis.tryParsingFuzzyDate(input);
				if (matches && matches[3] === undefined) {
					for (var i in this.durationTypes) {
						var duration = this.durationTypes[i];
						if (duration.indexOf(matches[2]) !== -1)
							suggestions.push(matches[1] + " "
								+ (matches[1] === 1 ? duration : openhmis.pluralize(duration)));
					}
				}
			}
			else
				var resultCallback = arguments[0];
			resultCallback(suggestions);
		},
		
		onSelect: function(event, ui) {
			var lengthInSeconds = openhmis.fromFuzzyDate(ui.item.value);
			var startDate = this.$startDate.datepicker("getDate");
			if (startDate) {
				var endDate = new Date(startDate.getTime() + (lengthInSeconds * 1000));
				this.setEndDate(endDate);
			}
		},
		
		showDatePicker: function(event) {
			this.$text.datepicker("option", "disabled", false);
			this.$text.datepicker("option", "onClose", this.onDatepickerClose);
			this.$text.datepicker("show");
		},
		
		setStartDate: function(date) {
			this.setDate("Start", date);
			this.$endDate.datepicker("option", "minDate", new Date(date.getTime() + 1000*60*60*24));
		},

		setEndDate: function(date) {
			this.setDate("End", date);
		},
		
		setDate: function(startOrEnd, date) {
			var $dateEl = startOrEnd === "Start" ? this.$startDate : this.$endDate;
			var $buttonEl = $dateEl.siblings(".edit-"+startOrEnd.toLowerCase()+"-date");

			$dateEl.datepicker("setDate", date);
			var title = __(startOrEnd + " date: ") + $dateEl.val() + __(" (edit)");
			$buttonEl.attr("title", title);
			this.updateTextDate();
		},
		
		updateTextDate: function() {
			var start = this.$startDate.datepicker("getDate");
			var end = this.$endDate.datepicker("getDate");
			if (start && end) {
				var length = (end.getTime() - start.getTime()) / 1000;
				this.$text.val(openhmis.toFuzzyDate(length));
			}
		},
		
		render: function() {
			var template = this.getTemplate();
			this.$el.html(template());
			editors.Autocomplete.prototype.render.call(this);
			
			this.$startDate = this.$("input.start-date");
			this.$endDate = this.$("input.end-date");
			var self = this;
			var datepickerOptions = {
				onSelect: this.onDatepickerSelect
			}
			this.$startDate.datepicker({
				showButtonPanel: true,
				onSelect: function(value, inst) { self.setStartDate($(this).datepicker("getDate")) }
			});			
			this.$endDate.datepicker({
				showButtonPanel: true,
				onSelect: function(value, inst) { self.setEndDate($(this).datepicker("getDate")) }
			});
			this.setStartDate(new Date());  // Default start date to today
			this.$(".edit-start-date").click(function() { self.$startDate.datepicker("show"); });
			this.$(".edit-end-date").click(function() { self.$endDate.datepicker("show"); });
			return this;
		}
	});
});