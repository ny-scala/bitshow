(function($){
  $(function(){
    window.bitshow = (function() {
      var api = 'http://' + window.location.host;
      return {
        converters: function(fn) {
          $.getJSON(api + '/converters', fn);
        },
        convert: function(converter, id, fn) {
          $.post(api + '/convert/' + id + "/" + converter, fn);
        },
        images: function(fn) {
          $.getJSON(api + "/images", fn);
        }

      };
    });
  });
})(jQuery);