package pt.ulisboa.tecnico.socialsoftware.tutor.exceptions;

public enum ErrorMessage {
    USER_HAS_QUIZ_ANSWERS("User %s has quiz answers"),
    USER_HAS_QUESTION_SUBMISSIONS("User %s has question submissions"),
    USER_HAS_DISCUSSIONS("User %s has discussions"),
    USER_HAS_REPLIES("User %s has replies"),

    INVALID_ACADEMIC_TERM_FOR_COURSE_EXECUTION("Invalid academic term for course execution"),
    INVALID_ACRONYM_FOR_COURSE_EXECUTION("Invalid acronym for course execution"),
    INVALID_CONTENT_FOR_OPTION("Invalid content for option"),
    INVALID_CONTENT_FOR_QUESTION("Invalid content for question"),
    INVALID_NAME_FOR_COURSE("Invalid name for course"),
    INVALID_NAME_FOR_TOPIC("Invalid name for topic"),
    INVALID_SEQUENCE_FOR_OPTION("Invalid sequence for option"),
    INVALID_SEQUENCE_FOR_QUESTION_ANSWER("Invalid sequence for question answer"),
    INVALID_QUIZ_ANSWER_SEQUENCE("Invalid quiz answer sequence: %s"),
    INVALID_TITLE_FOR_ASSESSMENT("Invalid title for assessment"),
    INVALID_TITLE_FOR_QUESTION("Invalid title for question"),
    INVALID_URL_FOR_IMAGE("Invalid url for image"),
    INVALID_TYPE_FOR_COURSE("Invalid type for course"),
    INVALID_TYPE_FOR_COURSE_EXECUTION("Invalid type for course execution"),
    INVALID_AVAILABLE_DATE_FOR_QUIZ("Invalid available date for quiz"),
    INVALID_CONCLUSION_DATE_FOR_QUIZ("Invalid conclusion date for quiz"),
    INVALID_RESULTS_DATE_FOR_QUIZ("Invalid results date for quiz"),
    INVALID_TITLE_FOR_QUIZ("Invalid title for quiz"),
    INVALID_TYPE_FOR_QUIZ("Invalid type for quiz"),
    INVALID_QUESTION_SEQUENCE_FOR_QUIZ("Invalid question sequence for quiz"),
    INVALID_TYPE_FOR_AUTH_USER("Invalid type for auth user"),
    INVALID_AUTH_USERNAME("Username: %s, belongs to a different authentication method"),
    INVALID_DATE_INTERVAL("Invalid date interval"),
    INVALID_QUESTION_TYPE("Invalid question type: %s"),

    ASSESSMENT_NOT_FOUND("Assessment not found with id %d"),
    COURSE_EXECUTION_NOT_FOUND("Course execution not found with id %d"),
    COURSE_EXECUTION_NOT_FOUND_STR("Course execution not found with id %s"),
    COURSE_EXECUTION_NOT_EXTERNAL("The course execution with id %d is not external"),
    COURSE_EXECUTION_NO_END_DATE("Course execution does not have an end date"),
    INVALID_EMAIL("The mail %s is invalid."),
    INVALID_PASSWORD("The password %s is invalid."),
    INVALID_ROLE("The Role %s is invalid."),
    FILL_IN_SPOT_NOT_FOUND("Fill in spot not found with id %d"),
    FILL_IN_SPOT_HAS_NO_CORRECT_OPTION("Fill in spot has no correct option"),
    ORDER_SLOT_NOT_FOUND("Order slot not found with id %d"),
    QUESTION_ANSWER_HAS_DISCUSSION("Question answers has discussion"),
    QUESTION_ANSWER_NOT_FOUND("Question answer not found with id %d"),
    QUESTION_NOT_FOUND("Question not found with id %d"),
    QUESTION_TYPE_NOT_IMPLEMENTED("Question type %s not implemented"),
    QUIZ_ANSWER_NOT_FOUND("Quiz answer not found with id %d"),
    QUIZ_NOT_FOUND("Quiz not found with id %d"),
    QUIZ_QUESTION_NOT_FOUND("Quiz question not found with id %d"),
    QUIZ_HAS_TOURNAMENT("Quiz has tournament"),
    TOPIC_CONJUNCTION_NOT_FOUND("Topic Conjunction not found with id %d"),
    TOPIC_NOT_FOUND("Topic not found with id %d"),
    USER_NOT_FOUND("User not found with id %d"),
    AUTHUSER_NOT_FOUND("AuthUser not found with id %d"),
    AUTHUSER_NOT_FOUND_BY_USERNAME("AuthUser not found with username %s"),
    EXTERNAL_USER_NOT_FOUND("User not found with username %s"),
    COURSE_NOT_FOUND("Course not found with name %s"),
    INVALID_COURSE("The course you are exporting from does not match the course you are importing to."
            + "If you want to import anyway, edit the .xml file and replace the second line to: %s"),
    USER_ALREADY_ACTIVE("User is already active with username %s"),
    USER_IS_ACTIVE("User state is active: username %s"),
    INVALID_CONFIRMATION_TOKEN("Invalid confirmation token"),
    EXPIRED_CONFIRMATION_TOKEN("Expired confirmation token"),
    INVALID_LOGIN_CREDENTIALS("Invalid login credentials"),

    CANNOT_DELETE_COURSE_EXECUTION("The course execution cannot be deleted %s"),

    QUESTION_KEY_ALREADY_EXISTS("Questions already exists with key %d"),
    QUESTION_OPTION_MISMATCH("Question %d does not have option %d"),
    QUESTION_ORDER_SLOT_MISMATCH("Question %d does not have slot %d"),

    DUPLICATE_TOPIC("Duplicate topic: %s"),
    DUPLICATE_USER("Duplicate user: %s"),
    DUPLICATE_COURSE_EXECUTION("Duplicate course execution: %s"),

    USERS_IMPORT_ERROR("Error importing users: %s"),
    QUESTIONS_IMPORT_ERROR("Error importing questions: %s"),
    TOPICS_IMPORT_ERROR("Error importing topics: %s"),
    ANSWERS_IMPORT_ERROR("Error importing answers: %s"),
    QUIZZES_IMPORT_ERROR("Error importing quizzes: %s"),

    QUESTION_IS_USED_IN_QUIZ("Question is used in quiz %s"),
    USER_NOT_ENROLLED("%s - Not enrolled in any available course"),
    QUIZ_NO_LONGER_AVAILABLE("This quiz is no longer available"),
    QUIZ_NOT_YET_AVAILABLE("This quiz is not yet available"),
    CANNOT_START_QRCODE_QUIZ("This is a QRCode only quiz"),
    NOT_QRCODE_QUIZ("Not a QRCode only quiz"),

    DUPLICATE_TOURNAMENT_PARTICIPANT("Duplicate tournament participant: %s"),
    STUDENT_NO_COURSE_EXECUTION("Student has no matching course execution : %s"),
    TEACHER_NO_COURSE_EXECUTION("Teacher has no matching course execution : %s"),
    TOURNAMENT_ALREADY_CLOSED("Tournament %s has already ended. You can not change it now"),
    TOURNAMENT_IS_OPEN("Tournament %s is live. You can not change it now"),
    TOURNAMENT_MUST_HAVE_ONE_TOPIC("Tournament must have at least one topic"),
    TOURNAMENT_MISSING_USER("Tournament requires a user"),
    TOURNAMENT_MISSING_START_TIME("Tournament requires the definition of the start date"),
    TOURNAMENT_MISSING_END_TIME("Tournament requires the definition of the end date"),
    TOURNAMENT_MISSING_TOPICS("Tournament requires the definition of topics"),
    TOURNAMENT_MISSING_NUMBER_OF_QUESTIONS("Tournament requires the definition of the number of questions"),
    TOURNAMENT_MAX_NUMBER_OF_QUESTIONS_EXCEEDED("Tournament %d max number of questions exceeded"),
    TOURNAMENT_NOT_CONSISTENT("Field %s of tournament is not consistent"),
    TOURNAMENT_NOT_FOUND("Tournament %s not found"),
    TOURNAMENT_TOPIC_COURSE("Tournament topics must be of the same course execution"),
    TOURNAMENT_NOT_OPEN("Tournament not open: %s"),
    TOURNAMENT_CANCELED("Tournament canceled: %s"),
    USER_NOT_JOINED("User has not joined tournament: %s"),
    USER_ALREDAY_ANSWERED_TOURNAMENT_QUIZ("User has already answered quiz of tournament: %s"),
    WRONG_TOURNAMENT_PASSWORD("Wrong tournament password for private tournament %d"),

    NO_CORRECT_OPTION("Question does not have a correct option"),
    NOT_ENOUGH_QUESTIONS("Not enough questions to create a quiz"),
    NOT_ENOUGH_QUESTIONS_TOURNAMENT("Not enough questions to create a quiz. One of the selected topics might not be available now. Please go check it"),
    AT_LEAST_ONE_OPTION_NEEDED("Questions need to have at least one option."),
    AT_LEAST_THREE_SLOTS_NEEDED("Order questions need to have at least 3 used slots."),
    ONE_CORRECT_OPTION_NEEDED("Questions need to have 1 and only 1 correct option"),
    CANNOT_CHANGE_ANSWERED_QUESTION("Can not change answered question"),
    QUIZ_HAS_ANSWERS("Quiz already has answers"),
    QUIZ_HAS_NO_ANSWERS("Quiz has no answers"),
    QUIZ_ALREADY_COMPLETED("Quiz already completed"),
    QUIZ_CANNOT_HAVE_REPEATED_QUESTIONS("Quiz cannot have repeated questions"),
    QUIZ_QUESTION_HAS_ANSWERS("Quiz question has answers"),
    FENIX_ERROR("Fenix Error"),
    AUTHENTICATION_ERROR("Authentication Error"),
    FENIX_CONFIGURATION_ERROR("Incorrect server configuration files for fenix"),
    REPLY_MISSING_DATA("Missing information for reply"),
    DUPLICATE_DISCUSSION("Duplicate discussion"),
    DISCUSSION_MISSING_MESSAGE("Missing message for discussion"),
    DISCUSSION_DATE_MISSING("Date is missing!"),
    DISCUSSION_MISSING_QUESTION("Missing question for discussion"),
    DISCUSSION_NOT_FOUND("Discussion not found: %d"),
    REPLY_NOT_FOUND("Reply not found: %d"),
    REPLY_UNAUTHORIZED_USER("Reply is not authorized!"),
    REPLY_MISSING_MESSAGE("Reply missing message"),
    CLOSE_NOT_POSSIBLE("Impossible to close discussion with no replies"),

    QUESTION_SUBMISSION_MISSING_QUESTION("Missing question for question submission"),
    QUESTION_SUBMISSION_MISSING_STUDENT("Missing student for question submission"),
    QUESTION_SUBMISSION_MISSING_COURSE("Question submission is not assigned to a course"),
    REVIEW_MISSING_COMMENT("Review must have comment"),
    REVIEW_MISSING_QUESTION_SUBMISSION("Review is missing associated question submission"),
    REVIEW_MISSING_USER("Review is missing associated user"),
    INVALID_TYPE_FOR_REVIEW("Invalid type for review"),
    QUESTION_SUBMISSION_NOT_FOUND("Question submission not found with id %d"),
    CANNOT_DELETE_REVIEWED_QUESTION("Question submission already in review or reviewed by teacher cannot be deleted"),
    CANNOT_DELETE_SUBMITTED_QUESTION("Question submission cannot be deleted by teacher"),
    CANNOT_EDIT_REVIEWED_QUESTION("Question submission in review by teacher cannot be edited"),
    CANNOT_REVIEW_QUESTION_SUBMISSION("Question submission already reviewed by teacher cannot be reviewed again"),

    CANNOT_CONCLUDE_QUIZ("Cannot conclude quiz."),

    DASHBOARD_NOT_FOUND("Dashboard not found: %d"),
    STUDENT_ALREADY_HAS_DASHBOARD("The student already has a dashboard for this course execution"),

    TEACHER_ALREADY_HAS_DASHBOARD("The teacher already has a dashboard for this course execution"),

    STUDENT_STATS_ALREADY_EXISTS("Student stats for this course execution (%d) already exists in this teacher dashboard"),
    STUDENT_STATS_INCORRECT_COURSE("Student stats course (%d) does not match teacher dashboard's course (%d)"),
    STUDENT_STATS_NOT_FOUND("Student stats does not belong to teacher dashboard"),
    
    QUIZ_STATS_ALREADY_EXISTS("Quiz stats for this course execution (%d) already exists in this teacher dashboard"),
    QUIZ_STATS_INCORRECT_COURSE("Quiz stats course (%d) does not match teacher dashboard's course (%d)"),
    QUIZ_STATS_NOT_FOUND("Quiz stats does not belong to teacher dashboard"),

    QUESTION_STATS_ALREADY_EXISTS("Question stats for this course execution (%d) already exists in this teacher dashboard"),
    QUESTION_STATS_INCORRECT_COURSE("Question stats course (%d) does not match teacher dashboard's course (%d)"),
    QUESTION_STATS_NOT_FOUND("Question stats does not belong to teacher dashboard"),

    ACCESS_DENIED("You do not have permission to view this resource"),
    CANNOT_OPEN_FILE("Cannot open file"),

    INVALID_CSV_FILE_FORMAT("The csv file uploaded has a wrong format"),
    WRONG_FORMAT_ON_CSV_LINE("Csv File has a wrong format on line: %d"),

    FRAUD_SERVICE_QUIZ_TYPE_NOT_SUPPORTED("Fraud service only supports timed, one way, in class quizzes"),

    WEEKLY_SCORE_ALREADY_CREATED("Weekly Score already created for this week"),
    CANNOT_CLOSE_CURRENT_WEEK("Cannot close current week: %s"),

    DIFFICULT_QUESTION_NOT_FOUND("Difficult question not found"),
    DIFFICULT_QUESTION_ALREADY_CREATED("Difficult question already created"),
    DIFFICULT_QUESTION_ALREADY_REMOVED("Difficult question already removed"),
    CANNOT_CREATE_DIFFICULT_QUESTION("Cannot create difficult question"),
    CANNOT_REMOVE_DIFFICULT_QUESTION("Cannot remove difficult question"),

    FAILED_ANSWER_NOT_FOUND("Failed answer not found"),
    FAILED_ANSWER_ALREADY_CREATED("Failed answer already created"),
    CANNOT_CREATE_FAILED_ANSWER("Cannot create failed answer"),
    CANNOT_REMOVE_FAILED_ANSWER("Cannot remove failed answer");

    public final String label;

    ErrorMessage(String label) {
        this.label = label;
    }
}