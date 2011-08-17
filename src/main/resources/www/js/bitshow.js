(function($){
  $(function(){
    window.bitshow = (function() {
      var api = window.location.host + "/api"
      return {
        converters: function(fn) {
          /*$.getJSON(api + '/converters', function(converters){
            console.log(converters);
            fn(converters);
          })*/
          fn([{name:"test filter", id:"test-filter"}, {name:"test filter 2", id:"test-filter-2"}])
        },
        apply: function(converter, id, fn) {
          /*$.getJSON(api + '/apply/'+ id, function(cid) {
            fn(cid);
          })*/
          console.log(converter, id)
        }
      };
    });
  });
})(jQuery);