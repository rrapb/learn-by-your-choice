$(document).ready(function() {
    $("#submitButton").click(function(event) {
        $("#spinner").removeAttr("hidden");
        $("#submitButton").attr("disabled");
        event.preventDefault();
        if(!validate()) {
            $("#spinner").attr("hidden", "");
            $("#submitButton").removeAttr("disabled");
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
        var valid = true;

        if(name === "" || name.length < 3) {
            $("#alert").removeAttr("hidden");
            $("#name").addClass("border-bottom-danger");
            valid = false;
        }
        else {
            $("#name").removeClass("border-bottom-danger");
            $("#name").addClass("border-bottom-success");
        }
        return valid;
    }
});