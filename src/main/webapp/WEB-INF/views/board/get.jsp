<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%@ include file="../includes/header.jsp"%>

<!-- Begin Page Content -->
<div class="container-fluid">

  <!-- Page Heading -->
  <h1 class="h3 mb-2 text-gray-800">Board Register</h1>

  <!-- DataTales Example -->
  <div class="card shadow mb-4">
    <div class="card-header py-3">
      <h6 class="m-0 font-weight-bold text-primary">Board Register</h6>
    </div>
    <div class="card-body">

        <div class="form-group">
          <label>Bno</label>
          <input type="text" name="bno" readonly="readonly"
                 value="<c:out value='${board.bno }' />" class="form-control">
        </div>

        <div class="form-group">
          <label>Title</label>
          <input type="text" name="title" readonly="readonly"
                 value="<c:out value='${board.title }' />" class="form-control">
        </div>

        <div class="form-group">
          <label>Text area</label>
          <textarea rows="3" name="content" class="form-control"
                    readonly="readonly"><c:out value="${board.content }" /></textarea>
        </div>

        <div class="form-group">
          <label>Writer</label>
          <input type="text" name="writer" readonly="readonly"
                 value="<c:out value='${board.writer}' />" class="form-control">
        </div>

        <button data-oper="modify" class="btn btn-default">Modify</button>
        <button data-oper="list" class="btn btn-info">List</button>

        <form id="operForm" action="board/modify" method="get">
            <input type="hidden" id="bno" name="bno" value="<c:out value='${board.bno}' />">
            <input type="hidden" id="pageNum" name="pageNum" value="<c:out value='${cri.pageNum}' />">
            <input type="hidden" id="amount" name="amount" value="<c:out value='${cri.amount}' />">
            <input type="hidden" name="type" value="<c:out value='${pageMaker.cri.type}' />">
            <input type="hidden" name="keyword" value="<c:out value='${pageMaker.cri.keyword}' />">
        </form>

    </div>
  </div>

    <div class="card shadow mb-4">
        <div class="card-header py-3">
            <h6 class="m-0 font-weight-bold text-primary">
                <i class="fa fa-comments fa-fw"></i> Reply
                <button id="addReplyBtn" class="btn btn-primary btn-xs float-right">New Reply</button>
            </h6>
        </div>
        <div class="card-body">

            <ul class="chat" style="padding-left: 0;">
                <!-- start reply -->
                <li class="left clearfix" data-rno="12" style="list-style: none;">
                    <div>
                        <div class="header">
                            <strong class="primary-font">user00</strong>
                            <small class="float-right text-muted">2018-01-01 13:00</small>
                        </div>
                        <p>Good job!</p>
                    </div>
                </li>
                <!-- end reply -->
            </ul>

        </div>
    </div>

    <!-- Modal -->
    <div class="modal fade" id="myModal" tabindex="-1" role="dialog"
        aria-labelledby="myModalLabel" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal"
                        aria-hidden="true">&times;</button>
                    <h4 class="modal-title" id="myModalLabel">REPLY MODAL</h4>
                </div>
                <div class="modal-body">
                    <div class="form-group">
                        <label>Reply</label>
                        <input type="text" name="reply" class="form-control" value="New Reply!!!!">
                    </div>
                    <div class="form-group">
                        <label>Replyer</label>
                        <input type="text" name="replyer" class="form-control" value="replyer">
                    </div>
                    <div class="form-group">
                        <label>Reply Date</label>
                        <input name="replyDate" class="form-control" value="">
                    </div>
                </div>
                <div class="modal-footer">
                    <button id="modalModBtn" class="btn btn-warning" type="button">Modify</button>
                    <button id="modalRemoveBtn" class="btn btn-danger" type="button">Remove</button>
                    <button id="modalRegisterBtn" class="btn btn-primary" type="button">Register</button>
                    <button id="modalCloseBtn" class="btn btn-default" data-dismiss="modal" type="button">Close</button>
                </div>
            </div>
            <!-- /.modal-content -->
        </div>
        <!-- /.modal-dialog -->
    </div>
    <!-- /.Modal -->

</div>
<!-- /.container-fluid -->

<script type="text/javascript" src="/resources/js/reply.js"></script>

<script type="text/javascript">
    $(document).ready(function() {

        var bnoValue = '<c:out value="${board.bno}"/>';
        var replyUL = $(".chat");

        showList(1);

        function showList(page) {

            replyService.getList({bno:bnoValue, page:page||1}, function (list) {

                var str = "";
                if(list == null || list.length == 0) {
                    replyUL.html("");

                    return;
                }
                for(var i = 0, len = list.length || 0; i < len; i++) {
                    str += "<li class='left clearfix' data-rno='" + list[i].rno + "' style='list-style: none;'>"
                        + "<div><div class='header'><strong class='primary-font'>" + list[i].replyer +"</strong>"
                        + "<small class='float-right text-muted'>" + replyService.displayTime(list[i].replyDate)
                        + "</small></div>"
                        + "<p>" + list[i].reply + "</p></div></li>";
                }

                replyUL.html(str);
            });
        } // end showList

        // modal
        var modal = $(".modal");
        var modalInputReply = modal.find("input[name='reply']");
        var modalInputReplyer = modal.find("input[name='replyer']");
        var modalInputReplyDate = modal.find("input[name='replyDate']");

        var modalModBtn = $("#modalModBtn");
        var modalRemoveBtn = $("#modalRemoveBtn");
        var modalRegisterBtn = $("#modalRegisterBtn");

        $("#addReplyBtn").on("click", function (e) {

            modal.find("input").val("");
            modalInputReplyDate.closest("div").hide();
            modal.find("button[id != 'modalCloseBtn']").hide();

            modalRegisterBtn.show();

            $(".modal").modal("show");
        });

        modalRegisterBtn.on("click", function (e) {

            var reply = {
                reply : modalInputReply.val(),
                replyer : modalInputReplyer.val(),
                bno:bnoValue
            };
            replyService.add(reply, function(result) {

                alert(result);

                modal.find("input").val("");
                modal.modal("hide");

                showList(1);
            });
        });


    });
</script>

<script type="text/javascript">
    $(document).ready(function() {

        var operForm = $('#operForm');

        $("button[data-oper='modify']").on("click", function (e) {

            operForm.attr("action", "/board/modify").submit();

        });

        $("button[data-oper='list']").on("click", function (e) {

            operForm.find("#bno").remove();
            operForm.attr("action", "/board/list");
            operForm.submit();
        });
    });
</script>

</div>
<!-- End of Main Content -->

<!-- Footer -->
<%@include file="../includes/footer.jsp"%>
