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
            type: "POST",
            url: "/addToolDetail",
            data: $("#addToolDetail").serialize(),
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
        var value = $("#value").val();
        var toolId = $("#toolId").val();
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
        if(value === "" || value.length < 3) {
            $("#alert").removeAttr("hidden");
            $("#value").addClass("border-bottom-danger");
            valid = false;
        }
        else {
            $("#value").removeClass("border-bottom-danger");
            $("#value").addClass("border-bottom-success");
        }
        if(toolId === "" || toolId <= 0) {
            $("#alert").removeAttr("hidden");
            $("#toolId").addClass("border-bottom-danger");
            valid = false;
        }
        else {
            $("#toolId").removeClass("border-bottom-danger");
            $("#toolId").addClass("border-bottom-success");
        }
        return valid;
    }
});