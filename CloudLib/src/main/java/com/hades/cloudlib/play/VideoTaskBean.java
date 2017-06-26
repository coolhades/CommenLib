package com.hades.cloudlib.play;

import java.util.List;

/**
 * Created by Hades on 2016/12/29.
 */

public class VideoTaskBean {


    /**
     * item_id : TASKITEM_A
     * item_type : section
     * item_vid : SECTIONVER-954
     * item_name : 这是一个加密的视频
     * item_album : http://3-img.bokecc.com/comimage/EB85B37C4546E6A4/2016-09-21/5DEC031AC938B0319C33DC5901307461-0.jpg
     * user_act : {"lock_status":"1","pass_status":"1","section_watch_time":"0"}
     * item_datas : {"video":{"user_id":"SECTION_UID-0001","node_caption":"第一章 两船兵开局","video_type":"cc","video_value":"5DEC031AC938B0319C33DC5901307461","video_config":"admin","video_duration":"65","video_size":"0"},"exam":[{"exam_vid":"EXAM-VID-018","can_close":"0","video_point_type":"mid","video_point_sec":"10"},{"exam_vid":"EXAM-VID-018","can_close":"0","video_point_type":"mid","video_point_sec":"20"},{"exam_vid":"EXAM-VID-018","can_close":"0","video_point_type":"mid","video_point_sec":"30"}]}
     */

    private String item_id;
    private String item_type;
    private String item_vid;
    private String item_name;
    private String item_album;
    private UserActBean user_act;
    private ItemDatasBean item_datas;

    public String getItem_id() {
        return item_id;
    }

    public void setItem_id(String item_id) {
        this.item_id = item_id;
    }

    public String getItem_type() {
        return item_type;
    }

    public void setItem_type(String item_type) {
        this.item_type = item_type;
    }

    public String getItem_vid() {
        return item_vid;
    }

    public void setItem_vid(String item_vid) {
        this.item_vid = item_vid;
    }

    public String getItem_name() {
        return item_name;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }

    public String getItem_album() {
        return item_album;
    }

    public void setItem_album(String item_album) {
        this.item_album = item_album;
    }

    public UserActBean getUser_act() {
        return user_act;
    }

    public void setUser_act(UserActBean user_act) {
        this.user_act = user_act;
    }

    public ItemDatasBean getItem_datas() {
        return item_datas;
    }

    public void setItem_datas(ItemDatasBean item_datas) {
        this.item_datas = item_datas;
    }

    public static class UserActBean {
        /**
         * lock_status : 1
         * pass_status : 1
         * section_watch_time : 0
         */

        private String lock_status;
        private String pass_status;
        private String section_watch_time;

        public String getLock_status() {
            return lock_status;
        }

        public void setLock_status(String lock_status) {
            this.lock_status = lock_status;
        }

        public String getPass_status() {
            return pass_status;
        }

        public void setPass_status(String pass_status) {
            this.pass_status = pass_status;
        }

        public String getSection_watch_time() {
            return section_watch_time;
        }

        public void setSection_watch_time(String section_watch_time) {
            this.section_watch_time = section_watch_time;
        }
    }

    public static class ItemDatasBean {
        /**
         * video : {"user_id":"SECTION_UID-0001","node_caption":"第一章 两船兵开局","video_type":"cc","video_value":"5DEC031AC938B0319C33DC5901307461","video_config":"admin","video_duration":"65","video_size":"0"}
         * exam : [{"exam_vid":"EXAM-VID-018","can_close":"0","video_point_type":"mid","video_point_sec":"10"},{"exam_vid":"EXAM-VID-018","can_close":"0","video_point_type":"mid","video_point_sec":"20"},{"exam_vid":"EXAM-VID-018","can_close":"0","video_point_type":"mid","video_point_sec":"30"}]
         */

        private VideoBean video;
        private List<ExamBean> exam;

        public VideoBean getVideo() {
            return video;
        }

        public void setVideo(VideoBean video) {
            this.video = video;
        }

        public List<ExamBean> getExam() {
            return exam;
        }

        public void setExam(List<ExamBean> exam) {
            this.exam = exam;
        }

        public static class VideoBean {
            /**
             * user_id : SECTION_UID-0001
             * node_caption : 第一章 两船兵开局
             * video_type : cc
             * video_value : 5DEC031AC938B0319C33DC5901307461
             * video_config : admin
             * video_duration : 65
             * video_size : 0
             */

            private String uid;
            private String node_caption;
            private String video_type;
            private String video_value;
            private String video_config;
            private String video_duration;
            private String video_size;

            public String getUid() {
                return uid;
            }

            public void setUid(String uid) {
                this.uid = uid;
            }

            public String getNode_caption() {
                return node_caption;
            }

            public void setNode_caption(String node_caption) {
                this.node_caption = node_caption;
            }

            public String getVideo_type() {
                return video_type;
            }

            public void setVideo_type(String video_type) {
                this.video_type = video_type;
            }

            public String getVideo_value() {
                return video_value;
            }

            public void setVideo_value(String video_value) {
                this.video_value = video_value;
            }

            public String getVideo_config() {
                return video_config;
            }

            public void setVideo_config(String video_config) {
                this.video_config = video_config;
            }

            public String getVideo_duration() {
                return video_duration;
            }

            public void setVideo_duration(String video_duration) {
                this.video_duration = video_duration;
            }

            public String getVideo_size() {
                return video_size;
            }

            public void setVideo_size(String video_size) {
                this.video_size = video_size;
            }
        }

        public static class ExamBean {
            /**
             * exam_vid : EXAM-VID-018
             * can_close : 0
             * video_point_type : mid
             * video_point_sec : 10
             */

            private String exam_vid;
            private String can_close;
            private String video_point_type;
            private String video_point_sec;

            public String getExam_vid() {
                return exam_vid;
            }

            public void setExam_vid(String exam_vid) {
                this.exam_vid = exam_vid;
            }

            public String getCan_close() {
                return can_close;
            }

            public void setCan_close(String can_close) {
                this.can_close = can_close;
            }

            public String getVideo_point_type() {
                return video_point_type;
            }

            public void setVideo_point_type(String video_point_type) {
                this.video_point_type = video_point_type;
            }

            public String getVideo_point_sec() {
                return video_point_sec;
            }

            public void setVideo_point_sec(String video_point_sec) {
                this.video_point_sec = video_point_sec;
            }
        }
    }
}
