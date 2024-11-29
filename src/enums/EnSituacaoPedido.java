package enums;

public enum EnSituacaoPedido {

        ABERTO("ABERTO"),
        A_CAMINHO("A_CAMINHO"),
        ENTREGUE("ENTREGUE");

        private String descricao;

        public String getDescricao() {
            return descricao;
        }

        private EnSituacaoPedido(String descricao) {
            this.descricao = descricao;
        }

}
