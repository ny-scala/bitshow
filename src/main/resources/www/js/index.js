(function($){
  $(function(){
    $("#bitpusher input[type='file']").change(function() {
      var show = function(){ $("#bitpusher input[type='submit']").fadeIn(); };
        setTimeout(show, 1500);
      });
      window.bitshow().images(function(imgs) {
        var pics = $("#bitpics");
        $.map(imgs.slice(0,10), function(i) {
          pics.append(['<img src="/images/',i,'"/>'].join(''));
        });
      });
  });
})(jQuery);