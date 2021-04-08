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
            url: "/addPlanProgram",
            data: $("#addPlanProgram").serialize(),
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
        var day = $("#day").val();
        var personId = $("#personId").val();
        var categoryId = $("#categoryId").val();
        var valid = true;
        if(day === "" || day.length < 3) {
            $("#alert").removeAttr("hidden");
            $("#day").addClass("border-bottom-danger");
            valid = false;
        }
        else {
            $("#day").removeClass("border-bottom-danger");
            $("#day").addClass("border-bottom-success");
        }
        if(personId === "" || personId <= 0) {
            $("#alert").removeAttr("hidden");
            $("#personId").addClass("border-bottom-danger");
            valid = false;
        }
        else {
            $("#personId").removeClass("border-bottom-danger");
            $("#personId").addClass("border-bottom-success");
        }
        if(categoryId === "" || categoryId <= 0) {
            $("#alert").removeAttr("hidden");
            $("#categoryId").addClass("border-bottom-danger");
            valid = false;
        }
        else {
            $("#categoryId").removeClass("border-bottom-danger");
            $("#categoryId").addClass("border-bottom-success");
        }
        return valid;
    }
});