package util;

/**
 * Enum representing modular system notification messages (Errors, Successes, Infos, Validations).
 */
public enum Notification {
    // === PLAYER MESSAGES ===
    PLAYER_NULL("ERR_PL_001", "❌ Lỗi: Đối tượng cầu thủ trống!"),
    DUPLICATE_PLAYER_ID("ERR_PL_002", "❌ Lỗi: Player ID '%s' đã tồn tại trong hệ thống!"),
    DUPLICATE_SHIRT_NUMBER("ERR_PL_003", "❌ Lỗi: Số áo %d đã có cầu thủ Active khác sử dụng!"),
    PLAYER_NOT_FOUND("ERR_PL_004", "❌ Lỗi: Không tìm thấy cầu thủ có ID '%s'!"),
    PLAYER_INACTIVE_ALREADY("ERR_PL_005", "❌ Lỗi: Cầu thủ '%s' đã ở trạng thái Inactive từ trước!"),
    SUCCESS_ADD_PLAYER("SUC_PL_001", "✅ Thêm mới cầu thủ thành công!"),
    SUCCESS_UPDATE_PLAYER("SUC_PL_002", "✅ Cập nhật thông tin cầu thủ thành công!"),
    SUCCESS_DEACTIVATE_PLAYER("SUC_PL_003", "✅ Đã chuyển trạng thái cầu thủ '%s' sang Inactive thành công!"),
    PLAYER_LIST_EMPTY("INF_PL_001", "ℹ️ Danh sách cầu thủ hiện tại đang trống!"),

    // === TRAINING MESSAGES ===
    SESSION_NULL("ERR_TR_001", "❌ Lỗi: Buổi tập luyện trống!"),
    DUPLICATE_SESSION_ID("ERR_TR_002", "❌ Lỗi: Training ID '%s' đã tồn tại!"),
    SESSION_NOT_FOUND("ERR_TR_003", "❌ Lỗi: Không tìm thấy buổi tập luyện có ID '%s'!"),
    SUCCESS_ADD_SESSION("SUC_TR_001", "✅ Thêm mới buổi tập luyện thành công!"),
    SUCCESS_DELETE_SESSION("SUC_TR_002", "✅ Đã xóa thành công buổi tập '%s' và bảng điểm danh liên quan!"),
    SUCCESS_RECORD_ATTENDANCE("SUC_TR_003", "✅ Ghi nhận điểm danh thành công!"),
    ATTENDANCE_NOT_FOUND("INF_TR_001", "ℹ️ Chưa có bản ghi điểm danh cho buổi tập: %s"),
    SESSION_LIST_EMPTY("INF_TR_002", "ℹ️ Chưa có buổi tập nào được lên lịch!"),

    // === MATCH MESSAGES ===
    MATCH_NULL("ERR_MA_001", "❌ Lỗi: Trận đấu thi đấu trống!"),
    DUPLICATE_MATCH_ID("ERR_MA_002", "❌ Lỗi: Match ID '%s' đã tồn tại!"),
    MATCH_NOT_FOUND("ERR_MA_003", "❌ Lỗi: Không tìm thấy trận đấu có ID '%s'!"),
    DUPLICATE_PERFORMANCE("ERR_MA_004", "❌ Lỗi: Cầu thủ '%s' đã có thống kê hiệu suất trong trận đấu '%s'!"),
    SUCCESS_ADD_MATCH("SUC_MA_001", "✅ Thêm mới trận đấu thành công!"),
    SUCCESS_RECORD_PERFORMANCE("SUC_MA_002", "✅ Lưu hiệu suất thi đấu của cầu thủ thành công!"),
    MATCH_LIST_EMPTY("INF_MA_001", "ℹ️ Chưa có trận đấu nào được ghi nhận!"),

    // === DATA PERSISTENCE MESSAGES ===
    DATA_LOAD_START("INF_DT_001", "🔄 Đang nạp dữ liệu từ hệ thống..."),
    DATA_LOAD_SUCCESS("SUC_DT_001", "✅ Nạp dữ liệu hoàn tất! (Đã tải %d cầu thủ)"),
    DATA_SAVE_START("INF_DT_002", "🔄 Đang lưu tất cả dữ liệu hệ thống vào bộ nhớ..."),
    DATA_SAVE_SUCCESS("SUC_DT_002", "✅ Lưu dữ liệu thành công! Tạm biệt."),

    // === ENTITY INPUT VALIDATION MESSAGES ===
    // Player validations
    VAL_PLAYER_ID("ERR_VAL_001", "❌ Lỗi: Player ID phải bắt đầu bằng 'PL' và chứa đúng 4 chữ số (e.g. PL0001)!"),
    VAL_PLAYER_NAME("ERR_VAL_002", "❌ Lỗi: Họ và tên không được để trống và chỉ chứa chữ cái!"),
    VAL_PLAYER_AGE("ERR_VAL_003", "❌ Lỗi: Tuổi cầu thủ chuyên nghiệp phải từ 16 đến 45!"),
    VAL_PLAYER_NATIONALITY("ERR_VAL_004", "❌ Lỗi: Quốc tịch không được để trống!"),
    VAL_PLAYER_POSITION("ERR_VAL_005", "❌ Lỗi: Vị trí phải thuộc một trong các loại: GK, DF, MF, FW!"),
    VAL_PLAYER_SHIRT("ERR_VAL_006", "❌ Lỗi: Số áo thi đấu phải từ 1 đến 99!"),
    VAL_PLAYER_SALARY("ERR_VAL_007", "❌ Lỗi: Lương cơ bản phải lớn hơn 0!"),
    VAL_PLAYER_STATUS_EMPTY("ERR_VAL_008", "❌ Lỗi: Trạng thái không được để trống!"),
    VAL_PLAYER_STATUS_INVALID("ERR_VAL_009", "❌ Lỗi: Trạng thái phải là Active hoặc Inactive!"),
    
    // Training session validations
    VAL_TRAINING_ID("ERR_VAL_010", "❌ Lỗi: Training ID phải bắt đầu bằng 'TR' và có đúng 4 chữ số (e.g. TR0001)!"),
    VAL_TRAINING_DATE("ERR_VAL_011", "❌ Lỗi: Ngày tập luyện phải có định dạng dd/MM/yyyy (e.g. 15/06/2026)!"),
    VAL_TRAINING_LOCATION("ERR_VAL_012", "❌ Lỗi: Địa điểm tập luyện không được để trống!"),
    VAL_TRAINING_TOPIC("ERR_VAL_013", "❌ Lỗi: Chủ đề tập luyện không được để trống!"),

    // Match validations
    VAL_MATCH_ID("ERR_VAL_014", "❌ Lỗi: Match ID phải bắt đầu bằng 'MA' và chứa đúng 4 chữ số (e.g. MA0001)!"),
    VAL_MATCH_DATE("ERR_VAL_015", "❌ Lỗi: Ngày thi đấu phải theo định dạng dd/MM/yyyy!"),
    VAL_MATCH_OPPONENT("ERR_VAL_016", "❌ Lỗi: Tên đối thủ không được để trống!"),
    VAL_MATCH_TYPE("ERR_VAL_017", "❌ Lỗi: Loại trận đấu phải là Friendly, League, hoặc Cup!"),

    // Performance validations
    VAL_PERF_GOALS("ERR_VAL_018", "❌ Lỗi: Số bàn thắng không được phép âm!"),
    VAL_PERF_ASSISTS("ERR_VAL_019", "❌ Lỗi: Số kiến tạo không được phép âm!"),
    VAL_PERF_YELLOW("ERR_VAL_020", "❌ Lỗi: Số thẻ vàng phải từ 0 đến 2!"),
    VAL_PERF_RED("ERR_VAL_021", "❌ Lỗi: Số thẻ đỏ phải là 0 hoặc 1!"),
    VAL_PERF_MINUTES("ERR_VAL_022", "❌ Lỗi: Số phút thi đấu phải từ 0 đến 120!");

    private final String code;
    private final String template;

    Notification(String code, String template) {
        this.code = code;
        this.template = template;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return template;
    }

    public String getMessage(Object... args) {
        try {
            return String.format(template, args);
        } catch (Exception e) {
            return template;
        }
    }

    @Override
    public String toString() {
        return "[" + code + "] " + template;
    }
}
// Force NetBeans to recompile this file
