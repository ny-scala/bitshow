(function($){
  $(function(){
    window.bitshow = (function() {
      var api = 'http://' + window.location.host;
      return {
        converters: function(fn) {
          $.getJSON(api + '/converters', function(converters){
            console.log(converters);
            fn(converters);
          })
        },
        convert: function(converter, id, fn) {
          var uri = api + '/convert/' + id + "/" + converter;
          $.post(uri, function(cid) {
            console.log(cid);
            fn(cid);
          });
        },
        images: function(fn) {
          $.getJSON(api + "/images", fn);
        }
      };
    });
  });
})(jQuery);