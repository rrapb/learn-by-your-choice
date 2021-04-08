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
            url: "/editTool",
            data: $("#editTool").serialize(),
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
        var description = $("#description").val();
        var categoryId = $("#categoryId").val();
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
        if(description === "" || description.length < 3) {
            $("#alert").removeAttr("hidden");
            $("#description").addClass("border-bottom-danger");
            valid = false;
        }
        else {
            $("#description").removeClass("border-bottom-danger");
            $("#description").addClass("border-bottom-success");
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