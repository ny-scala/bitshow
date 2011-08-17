(function($){
  $(function() {
    var b = window.bitshow(), sel = $("#converters");
    b.converters(function(converters){
      $.map(converters, function(c) {
         sel.append(['<option value="', c.id, '">', c.name, '</option>'].join(''));
      });
      sel.change(function(){
        var bits = $(".bits")
        console.log("apply " + sel.val() +" to " + bits.attr("id"));
        /*$(".bits").fadeOut(function(){
          $(this).attr("src", )
        });*/
      });
    });
  });
})(jQuery);