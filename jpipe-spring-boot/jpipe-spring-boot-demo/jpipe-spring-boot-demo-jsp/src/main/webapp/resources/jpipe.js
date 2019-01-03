var JP = {
    view: function (map) {
        var id = map['domId'];
        document.getElementById(id).innerHTML =
            map['data']['id'] + ' -> '
            + map['data']['name'] + ' -> '
            + map['templateId'];
    }
};