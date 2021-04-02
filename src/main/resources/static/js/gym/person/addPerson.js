$(document).ready(function() {
    $("#submitButton").click(function(event) {
        event.preventDefault();
        if(!validate()) {
            return;
        }
        $.ajax({
            type: "POST",
            url: "/addPerson",
            data: new FormData($("#addPerson")[0]),
            enctype: 'multipart/form-data',
            processData: false,
            contentType: false,
            cache: false,
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
        var firstName = $("#firstName").val();
        var lastName = $("#lastName").val();
        var personalId = $("#personalId").val();
        if(firstName == "" || firstName.length < 3) {
            $("#alert").removeAttr("hidden");
            $("#firstName").addClass("border-bottom-danger");
            return false;
        }else if(lastName == "" || lastName.length < 3) {
            $("#alert").removeAttr("hidden");
            $("#lastName").addClass("border-bottom-danger");
            return false;
        }else if(personalId == "" || personalId.length < 3) {
            $("#alert").removeAttr("hidden");
            $("#personalId").addClass("border-bottom-danger");
            return false;
        }
        return true;
    }
});