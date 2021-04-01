$(document).ready(function() {
    $("#submitButton").click(function(event) {
        event.preventDefault();
        if(!validate()) {
            return;
        }
        $.ajax({
            type: "PUT",
            url: "/editRole",
            data: $("#editRole").serialize(),
            success: function (result) {
                $("#page-top").html(result);
            },
            error: function (result) {
                console.log(result);
                $("#alert").removeAttr("hidden");
            }
        });
    });

    function validate() {
        var name = $("#name").val();
        if(name == "" || name.length < 3) {
            $("#alert").removeAttr("hidden");
            $("#name").addClass("border-bottom-danger");
            return false;
        }
        return true;
    }
});