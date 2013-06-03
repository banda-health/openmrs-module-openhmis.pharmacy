curl(
{ baseUrl: openhmis.url.resources },
[
	openhmis.url.backboneBase + 'js/lib/jquery',
	openhmis.url.backboneBase + 'js/openhmis',
	openhmis.url.backboneBase + 'js/lib/i18n',	
	openhmis.url.backboneBase + 'js/model/patient',
	openhmis.url.backboneBase + 'js/view/patient',
	openhmis.url.pharmacyBase + 'js/model/drug',
	openhmis.url.pharmacyBase + 'js/view/drug'
],
function($, openhmis, __) {	
	var Screen = function() {
		this.patientUuid = openhmis.getQueryStringParameter("patientUuid");
		
		this.patientView = new openhmis.PatientView();
		// Set up patient search selection handler
		openhmis.doSelectionHandler = this.patientView.takeRawPatient;
		
		this.createView.call(this);
	}
	
	Screen.prototype.createView = function(options) {
		var drugOrderList = new openhmis.GenericCollection([], { model: openhmis.DrugOrder });
		this.drugOrderEntryView = new openhmis.DrugOrderEntryView({
			model: drugOrderList,
			showRetiredOption: false,
			showPaging: false,
			itemActions: ["inlineEdit", "remove"]
		});

		var self = this;
		// If a patient is specified
		if (this.patientUuid) {
			var patient = new openhmis.Patient({ uuid: this.patientUuid });
			patient.fetch({
				silent: true,
				success: function(patient, resp) {
					self.setupViewWithPatient.call(self, patient, resp);
				}
			});
		}
		else {
			this.displayView.call(this);
		}
	}
	
	Screen.prototype.setupViewWithPatient = function(patient) {
		this.drugOrderEntryView.set("patient", patient);
		this.patientView.model = patient;
		this.displayView();
	}

	Screen.prototype.displayView = function() {
		this.patientView.setElement($('#patient-view'));
		this.patientView.render();
		$("#patient-view").after(this.drugOrderEntryView.render().el);
		this.drugOrderEntryView.setupNewItem();
		
		var self = this;
		this.patientView.on('selected', function(patient) {
			self.drugOrderEntryView.setPatient(patient);
			self.drugOrderEntryView.focus();
		});
		this.patientView.on('editing', this.drugOrderEntryView.blur);
		$("#saveOrders").click(function(event) {
			if (confirm(__("Are you sure you want to save the prescription(s)?")))
				self.drugOrderEntryView.save(event);
		});
	}
	
	$(document).ready(function() {
		var screen = new Screen();
	});
	
	return Screen;
});