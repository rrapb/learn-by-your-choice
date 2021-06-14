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
            url: "/editUser",
            data: $("#editUser").serialize(),
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
        var personId = $("#personId").val();
        var roleId = $("#roleId").val();
        var username = $("#username").val();
        var email = $("#email").val();
        var valid = true;

        if(personId === "" && personId <= 0) {
            $("#alert").removeAttr("hidden");
            $("#personId").addClass("border-bottom-danger");
            valid = false;
        }
        else {
            $("#personId").removeClass("border-bottom-danger");
            $("#personId").addClass("border-bottom-success");
        }

        if(roleId === "" || roleId <= 0) {
            $("#alert").removeAttr("hidden");
            $("#roleId").addClass("border-bottom-danger");
            valid = false;
        }
        else {
            $("#roleId").removeClass("border-bottom-danger");
            $("#roleId").addClass("border-bottom-success");
        }

        if(username === "" || username.length < 3) {
            $("#alert").removeAttr("hidden");
            $("#username").addClass("border-bottom-danger");
            valid = false;
        }
        else {
            $("#username").removeClass("border-bottom-danger");
            $("#username").addClass("border-bottom-success");
        }

        if(email.length < 3 || !validate_email(email) ) {
            $("#alert").removeAttr("hidden");
            $("#email").addClass("border-bottom-danger");
            valid = false;
        }
        else {
            $("#email").removeClass("border-bottom-danger");
            $("#email").addClass("border-bottom-success");
        }

        return valid;
    }
});

function validate_email(email) {
    var regex = /^(([^<>()[\]\\.,;:\s@"]+(\.[^<>()[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
    return regex.test(email);
}