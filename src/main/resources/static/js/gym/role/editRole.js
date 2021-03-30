$(document).ready(function() {
    $("#submitButton").click(function(event) {
        console.log("editRole");
        event.preventDefault();
        $.ajax({
            type: "POST",
            url: "/editRole",
            data: $("#editRole").serialize(),
            success: function (result) {
                if(result.isSaved){
                    $(".alert-success").removeAttr('hidden');
                    $(".alert-danger").attr('hidden','');
                    $("#editRole")[0].reset();
                }
                else{
                    $(".alert-danger").removeAttr('hidden');
                    $(".alert-success").attr('hidden','');
                }
            }
        });
    });
});