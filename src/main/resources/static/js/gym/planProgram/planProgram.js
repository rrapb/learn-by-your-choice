$(document).ready(function() {
    $("#downloadPlanProgramButton").click(function(event) {
        $("#spinner").removeAttr("hidden");
        $("#downloadPlanProgramButton").attr("disabled");
        $("#downloadPlanProgramForm").submit();
        $("#closeModal").click();
    });
});