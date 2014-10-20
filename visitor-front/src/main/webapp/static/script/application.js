function setupTabs() {
	// see http://jqueryui.com/demos/tabs/
	$(".tabs").tabs({
		load: function(event, ui) {
			setupPage();
		}
	});
}

function setupValidation() {
	// see https://github.com/jzaefferer/jquery-validation/
	var validator = $("form").validate();
	//$("input[class^=number]").validate();
}

function setupDatepickers() {
	// see http://docs.jquery.com/UI/Datepicker/formatDate
	$(".datepicker").datepicker({
		dateFormat : 'yy-mm-dd'
	});
}

function setupAjaxLinks() {
	// see http://api.jquery.com/jQuery.ajax/
	$(".ajax, .navigation").unbind('click').bind('click', function() {
		if ($(this).parents("div").find("#result").size() > 0) {
			var list = $(this).parents("div").find("#result");
			list.load($(this).attr("href") + " #main #result", function() {
				setupPage();
			});
		} else {
			$("#main").load($(this).attr("href") + " #main", function() { 
				setupPage();
			});
		}
		return false;
	});

	// see http://jqueryui.com/demos/dialog/#modal
	$(".modal").unbind('click').bind('click', function() {
		$("#modalDialog").load($(this).attr("href") + " #main", function() {
			 setupPage();
		});
		$("#modalDialog").dialog({modal: true});
		return false;
	});
}

function closeConfirm() {
	$(".ui-dialog-titlebar-close").click();
}

function setupAjaxForms() {
	$("#main form.search").unbind('submit').bind('submit', function(e) {
		var url = $(this).attr("action");
		var serializedForm = $(this).serialize();
		var element ;
		if ($(this).parents("div").find("#result").size() > 0) {
			element = $(this).parents("div").find("#result"); 
		} else {
			element = $("#main #result");
		}
		if (element) {
			$.post(url, serializedForm, function(data) {
				var list = $(data).find("#main #result");
				element.html(list);
				setupPage();
			});
		}
		return false;
	});

	$("#main form input[name='sp.searchPattern']").unbind('keyup').bind('keyup', function() {
		var url = $(this).closest("form").attr("action");
		var serializedForm = $(this).closest("form").serialize();
		var element;
		if ($(this).parents("div").find("#result").size() > 0) {
			element = $(this).parents("div").find("#result"); 
		} else {
			element = $("#main #result");
		}
		if (element) {
			$.post(url, serializedForm, function(data) {
				var list = $(data).find("#main #result");
				element.html(list);
				setupPage();
			});
		}
		return true;
	});
}

function setupButton(name, icon, showText) {
	// configuration at http://jqueryui.com/demos/button/#icons
	$("#main " + name).button({
		icons: {
			primary: "ui-icon-" + icon
		},
		text: showText
	});
}

function setupButtons() {
	$("button, input:submit, .button", "#main").button();
	// icon list available at http://jqueryui.com/themeroller/
	setupButton(".button-show", "zoomin", true);
	setupButton(".button-edit", "pencil", true);
	setupButton(".button-delete", "trash", true);
	setupButton(".button-link", "link", true);
	setupButton(".button-download", "document", true);
	setupButton(".button-list", "search", true);
	setupButton(".button-search", "search", true);
	setupButton(".button-add", "add", true);
	setupButton(".button-save", "check", true);
	setupButton(".button-create", "plusthick", true);
	setupButton(".button-show", "zoomin", true);
	setupButton(".button-login", "unlocked", true);
	setupButton(".button-show.miniature", "zoomin", false);
	setupButton(".button-edit.miniature", "pencil", false);
	setupButton(".button-delete.miniature", "trash", false);
}

function setupPage() {
	setupTabs();
	setupButtons();
	setupAjaxLinks();
	setupAjaxForms();
	setupValidation();
	setupDatepickers();
}

$(document).ready(function() {
	setupPage();
});
