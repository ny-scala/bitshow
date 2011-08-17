(function($){
  $(function() {
    var b = window.bitshow(), sel = $("#converters");
    b.converters(function(converters){
      $.map(converters, function(c) {
         sel.append(['<option value="', c, '">', c, '</option>'].join(''));
      });
      sel.change(function(){
        var bits = $(".bits")
        b.convert(encodeURI(sel.val()), bits.attr("id"), function(cid){
          bits.fadeOut(function(){
            $(this).attr("src", "/images/" + cid.id).fadeIn();
          });
        });
      });
    });
  });
})(jQuery);