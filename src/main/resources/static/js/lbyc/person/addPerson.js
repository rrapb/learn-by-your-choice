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
        var birthDate = $("#birthDate").val();
        var gender = $("input[name='gender']:checked").val();
        var valid = true;

        if(firstName === "" || firstName.length < 3) {
            $("#alert").removeAttr("hidden");
            $("#firstName").addClass("border-bottom-danger");
            valid = false;
        }
        else {
            $("#firstName").removeClass("border-bottom-danger");
            $("#firstName").addClass("border-bottom-success");
        }

        if(lastName === "" || lastName.length < 3) {
            $("#alert").removeAttr("hidden");
            $("#lastName").addClass("border-bottom-danger");
            valid = false;
        }
        else {
            $("#lastName").removeClass("border-bottom-danger");
            $("#lastName").addClass("border-bottom-success");
        }

        if(personalId === "" || personalId.length < 3) {
            $("#alert").removeAttr("hidden");
            $("#personalId").addClass("border-bottom-danger");
            valid = false;
        }
        else {
            $("#personalId").removeClass("border-bottom-danger");
            $("#personalId").addClass("border-bottom-success");
        }

        if(birthDate == null || birthDate === "") {
            $("#alert").removeAttr("hidden");
            $("#birthDate").addClass("border-bottom-danger");
            valid = false;
        }
        else {
            $("#birthDate").removeClass("border-bottom-danger");
            $("#birthDate").addClass("border-bottom-success");
        }

        if(gender !== "M" && gender !== "F"){
            valid = false;
        }
        return valid;
    }
});

$(".custom-file-input").on("change", function() {
    var fileName = $(this).val().split("\\").pop();
    $(this).siblings(".custom-file-label").addClass("selected").html(fileName);
});