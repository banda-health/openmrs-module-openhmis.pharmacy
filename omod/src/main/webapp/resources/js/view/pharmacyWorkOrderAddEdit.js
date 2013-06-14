define(
[
	openhmis.url.backboneBase + 'js/lib/jquery',
	openhmis.url.backboneBase + 'js/lib/underscore',
	openhmis.url.backboneBase + 'js/openhmis',
	openhmis.url.backboneBase + 'js/view/generic'
],
function($, _, openhmis) {
	var customAddEdit = openhmis.GenericAddEditView.extend({
		render: function() {
			openhmis.GenericAddEditView.prototype.render.call(this);
			this.$el.append("<p>This is so customized</p>");
		}
	});
	
	return customAddEdit;
});