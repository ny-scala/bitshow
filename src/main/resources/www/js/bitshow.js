(function($){
  $(function(){
    window.bitshow = (function() {
      var api = window.location.host;
      console.log(api);
      return {
        converters: function(fn) {
          $.getJSON('http://' + api + '/converters', function(converters){
            console.log(converters);
            fn(converters);
          })
        },
        convert: function(converter, id, fn) {
          var uri = 'http://' + api + '/convert/' + id + "/" + converter;
          console.log(uri);
          $.post(uri, function(cid) {
            console.log(cid);
            fn(cid);
          });
        }
      };
    });
  });
})(jQuery);