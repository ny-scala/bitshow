(function($){
  $(function() {
    var b = window.bitshow(), sel = $("#converters");
    b.converters(function(converters){
      $.map(converters, function(c) {
         sel.append(['<option value="', c, '">', c, '</option>'].join(''));
      });
      sel.change(function(){
        var bits = $(".bits")
        console.log("apply " + sel.val() +" to " + bits.attr("id"));
        b.convert(encodeURI(sel.val()), bits.attr("id"), function(cid){
          console.log("got " + cid);
          $(".bits").fadeOut(function(){
            $(this).attr("src", "/images/" + cid.id).fadeIn(function(){
              alert("kaboom!");
            });
          });
        });   
      });
    });
  });
})(jQuery);