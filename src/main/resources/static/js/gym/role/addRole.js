$(document).ready(function() {
    $("#submitButton").click(function(event) {
        // console.log("login");
        event.preventDefault();
        $.ajax({
            type: "POST",
            url: "/addRole",
            data: JSON.stringify( getFormData($("#addRole"))),
            dataType: "json",
            contentType: "application/json",
            success: function (result) {
                console.log(result);
            }
        });
    });

    function getFormData($form){
        var unindexed_array = $form.serializeArray();
        var indexed_array = {};

        $.map(unindexed_array, function(n, i){
            indexed_array[n['name']] = n['value'];
        });

        return indexed_array;
    }
});