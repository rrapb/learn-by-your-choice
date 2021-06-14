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
            url: "/addCourseMaterial",
            data: new FormData($("#addCourseMaterial")[0]),
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
        var name = $("#name").val();
        var fileSize = $("#file")[0].files[0].size / 1024 / 1024;
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

        if(fileSize > 50) {
            $("#fileSizeAlert").removeAttr("hidden");
            $("#fileDiv").addClass("border-bottom-danger");
            valid = false;
        }
        else {
            $("#fileDiv").removeClass("border-bottom-danger");
            $("#fileDiv").addClass("border-bottom-success");
        }

        return valid;
    }
});

$(".custom-file-input").on("change", function() {
    var fileName = $(this).val().split("\\").pop();
    $(this).siblings(".custom-file-label").addClass("selected").html(fileName);
});