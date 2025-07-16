-- +goose Up
-- +goose StatementBegin
CREATE TABLE [dbo].[Rating] (
    [Account]     UNIQUEIDENTIFIER NOT NULL CONSTRAINT FK_Rating_Account  FOREIGN KEY REFERENCES [dbo].[Account]([Id]),
    [Show]        UNIQUEIDENTIFIER NOT NULL CONSTRAINT FK_Rating_Show     FOREIGN KEY REFERENCES [dbo].[Show]([Id]),
    [Value]       INT              NOT NULL,
    [CreatedTime] DATETIME,
    [UpdatedTime] DATETIME,
    [IsDeleted]   BIT              NOT NULL CONSTRAINT DF_Rating_IsDeleted DEFAULT 0,
    CONSTRAINT PK_Rating PRIMARY KEY ([Account], [Show])
);
-- +goose StatementEnd


-- +goose Down
-- +goose StatementBegin
DROP TABLE [dbo].[Rating];
-- +goose StatementEnd
